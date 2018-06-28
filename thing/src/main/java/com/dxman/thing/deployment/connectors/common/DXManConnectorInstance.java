package com.dxman.thing.deployment.connectors.common;


import com.dxman.design.services.common.DXManServiceTemplate;
import com.dxman.execution.DXManWfInvocation;
import com.dxman.execution.DXManWfNode;
import com.dxman.utils.DXManIDGenerator;
import com.dxman.utils.RuntimeTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Damian Arellanes
 */
public abstract class DXManConnectorInstance {
    
  private final String id;
  private final DXManServiceTemplate managedService;    

  protected final Gson gson;

  public DXManConnectorInstance(DXManServiceTemplate managedService) {

    this.managedService = managedService;
    this.id = DXManIDGenerator.generateConnectorID();
    //this.id = managedService.getInfo().getName();

    RuntimeTypeAdapterFactory<DXManWfNode> adapter = RuntimeTypeAdapterFactory
      .of(DXManWfNode.class, "wfnode")
      .registerSubtype(DXManWfInvocation.class, "wfinvocation");
    gson = new GsonBuilder().disableHtmlEscaping()
      .registerTypeAdapterFactory(adapter).create();
  }

  public void init(String workflowJSON) {
    
    before();
    activate(workflowJSON);
    after();
  }

  private void before() {
      // Leave trace
  }

  private void after() {
      // TODO 
  }

  public abstract void activate(String workflowJSON);

  public String getId() { return id; }

  public DXManServiceTemplate getManagedService() { return managedService; }
}
