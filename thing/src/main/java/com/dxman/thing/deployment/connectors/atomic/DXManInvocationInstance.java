package com.dxman.thing.deployment.connectors.atomic;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.design.data.DXManOperation;
import com.dxman.design.distribution.*;
import com.dxman.design.services.common.DXManServiceTemplate;
import com.dxman.thing.deployment.connectors.common.DXManConnectorInstance;
import com.dxman.execution.DXManWfInvocation;
import com.dxman.utils.DXManMap;

/**
 * @author Damian Arellanes
 */
public class DXManInvocationInstance extends DXManConnectorInstance {
        
  private final DXManMap<DXManProtocol, InvocationHandler> invocationHandlers;
  private final DXManDataManager dataReader;

  public DXManInvocationInstance(DXManServiceTemplate managedService, 
    DXManDataSpace dataSpace, String nodeAlias) {

    super(managedService);

    invocationHandlers = new DXManMap<>();
    invocationHandlers.put(DXManProtocol.tcp, new SocketInvocator());
    invocationHandlers.put(DXManProtocol.http, new RestInvocator());

    dataReader = new DXManDataManager(nodeAlias, dataSpace, 
      managedService.getOperations());
  }

  @Override
  public void activate(String workflowJSON) {

    System.out.println("Invocation connector activated!");
   
    // Reads the operation (from the event) to be invoked in this service
    DXManWfInvocation flow = gson.fromJson(workflowJSON, DXManWfInvocation.class);
    String operationName = flow.getId();
    DXManOperation operationToInvoke = getManagedService().getOperations()
      .get(operationName);

    // Gets the operation from the managed service
    DXManBindingInfo bindingInfo = operationToInvoke.getBindingInfo();

    // Gets the input parameters and constructs JSON request
    String request = dataReader.read(operationToInvoke);

    // Invokes the operation        
    String result = invocationHandlers.get(
      DXManProtocol.valueOf(
        operationToInvoke.getBindingInfo().getEndpoint().getScheme()
      )
    ).invokeJSON(bindingInfo, request);

    // Writes output parameters (if any)
    dataReader.write(operationToInvoke, result);
  }
}
