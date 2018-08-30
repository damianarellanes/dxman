package com.dxman.thing.deployment.connectors.composite;

import com.dxman.execution.common.DXManWfNodeMapper;
import com.dxman.design.connectors.composition.*;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.common.DXManWfCondition;
import com.dxman.execution.selector.DXManWfSelector;
import com.dxman.execution.selector.DXManWfSelectorCustom;
import com.dxman.thing.deployment.connectors.common.DXManConnectorDataManager;
import com.dxman.thing.deployment.connectors.common.DXManConnectorInstance;
import com.dxman.thing.server.base.DXManConnectorRequester;
import com.google.gson.Gson;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Damian Arellanes
 */
public class DXManSelectorInstance extends DXManConnectorInstance {
  
  private class ConcurrentManager {
    
    public ConcurrentManager() {}

    public void sync() {
      ExecutorService executor = executeParallelChilds();
      joinParallelChilds(executor);
    }

    public void async() {
      executeParallelChilds();
    }

    private ExecutorService executeParallelChilds() {

      // Adds all the tasks into a set
      Set<Callable<String>> tasks = new HashSet<>();
      
      for(DXManWfNodeMapper subNodeMapper: subNodeMappers) {
        
        DXManWfCondition condition = ((DXManWfSelectorCustom)subNodeMapper
          .getCustom()).getCondition();
        
        if(connectorDataManager.matches(workflowId, 
          workflowTimestamp, condition)) {
          
          tasks.add((Callable) () -> {          
            transferControl(
              subNodeMapper.getNode(), subNodeMapper.getNode().getUri()
            );
            return null;
          });
        }
      }
      
      // Pool size = sum of tasks in the subnodes of the workflow tree
      ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
      
      // Invokes all tasks in parallel
      try {
        executor.invokeAll(tasks);
      } catch (InterruptedException ex) {
        System.err.println(ex);
      }
      
      // Terminates threads for the tasks
      executor.shutdown();
      
      return executor;
    }

    private void joinParallelChilds(ExecutorService executor) {
      while (!executor.isTerminated()) {}
    }
  }
  
  private interface Invocation {
    public void execute();
  }
        
  private class InvocationContext {

    private Invocation invocation;

    public InvocationContext() {}

    public void execute() {
      invocation.execute();
    }

    public void setInvocation(DXManParallelType invocationType) {

      if(invocationType.equals(DXManParallelType.SYNC)) {
        invocation = new SynchronousInvocation();
      } else invocation = new AsynchronousInvocation();      
    }
  }
    
  private class AsynchronousInvocation implements Invocation {

    private final ConcurrentManager concurrentManager;

    public AsynchronousInvocation() {
      this.concurrentManager = new ConcurrentManager();
    }

    @Override
    public void execute() {
      concurrentManager.async();
    }
  }

  private class SynchronousInvocation implements Invocation {

    private final ConcurrentManager concurrentManager;

    public SynchronousInvocation() {
      this.concurrentManager = new ConcurrentManager();
    }

    @Override
    public void execute() {
      concurrentManager.sync();
    }
  }
  
  private final InvocationContext invocationContext;
  private List<DXManWfNodeMapper> subNodeMappers;
  private final DXManConnectorDataManager connectorDataManager;
  private String workflowId;
  private String workflowTimestamp;    

  public DXManSelectorInstance(DXManCompositeServiceTemplate managedService, 
    DXManConnectorDataManager connectorDataManager, 
    DXManConnectorRequester requester, Gson gson) {
    
    super(managedService, managedService.getConnector().getName(), 
      requester, gson);
    
    this.connectorDataManager = connectorDataManager;
    
    invocationContext = new InvocationContext();    
    invocationContext.setInvocation(DXManParallelType.SYNC);
  }

  @Override
  public void activate(String workflowJSON) {
    
    System.err.println(this.getManagedService().getInfo().getName() 
      + " (Selector Connector) activated [" + new Date() + "]");
    
    DXManWfSelector flow = gson.fromJson(workflowJSON, DXManWfSelector.class);
    subNodeMappers = flow.getSubnodeMappers();
    workflowId = flow.getWorkflowId();
    workflowTimestamp = flow.getWorkflowTimestamp();
    
    invocationContext.execute();
  }
}
