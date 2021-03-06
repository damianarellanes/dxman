package com.dxman.thing.deployment.connectors.atomic;

import com.dxman.design.data.DXManOperation;
import com.dxman.design.distribution.*;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.thing.deployment.connectors.common.DXManConnectorInstance;
import com.dxman.execution.invocation.DXManWfInvocation;
import com.dxman.thing.server.base.DXManConnectorRequester;
import com.dxman.utils.DXManMap;
import com.google.gson.Gson;
import java.util.concurrent.*;

/**
 * @author Damian Arellanes
 */
public class DXManInvocationInstance extends DXManConnectorInstance {
        
  private final DXManMap<DXManProtocol, InvocationHandler> invocationHandlers;
  private final DXManInvocationDataManager invocationDataManager;

  public DXManInvocationInstance(DXManAtomicServiceTemplate managedService, 
    DXManConnectorRequester requester, Gson gson, 
    DXManInvocationDataManager invocationDataManager) {

    super(managedService, managedService.getConnector().getName(),
      requester, gson);

    invocationHandlers = new DXManMap<>();
    invocationHandlers.put(DXManProtocol.tcp, new SocketInvocator());
    invocationHandlers.put(DXManProtocol.http, new RestInvocator());

    this.invocationDataManager = invocationDataManager;
  }

  @Override
  public void activate(String workflowJSON) {
    
    // Reads the operation (from the event) to be invoked in this service
    DXManWfInvocation flow = gson.fromJson(workflowJSON, DXManWfInvocation.class);
    String operationName = flow.getId();
    DXManOperation operationToInvoke = getManagedService().getOperations()
      .get(operationName);

    //System.out.println("Executing operation: " + operationName);
    
    // Gets the operation from the managed service
    DXManBindingInfo bindingInfo = operationToInvoke.getBindingInfo();

    // Gets the input parameters and constructs JSON request
    String request = invocationDataManager.read(
      flow.getWorkflowId(), flow.getWorkflowTimestamp(), operationToInvoke
    );
        
    // Invokes the operation        
    String result = invocationHandlers.get(
      DXManProtocol.valueOf(
        operationToInvoke.getBindingInfo().getEndpoint().getScheme()
      )
    ).invokeJSON(bindingInfo, request);

    // Writes output parameters (if any) in background to maximize performance
    //invocationDataManager.write(flow.getWorkflowId(), operationToInvoke, result);
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(() -> {
      invocationDataManager.write(flow.getWorkflowId(), operationToInvoke, result);
    });
    executor.shutdown();
  }
}
