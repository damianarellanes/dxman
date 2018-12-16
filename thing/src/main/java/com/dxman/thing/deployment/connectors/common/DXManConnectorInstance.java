package com.dxman.thing.deployment.connectors.common;


import com.dxman.execution.common.DXManWfNode;
import com.dxman.design.services.common.DXManServiceTemplate;
import com.dxman.thing.server.base.DXManConnectorRequester;
import com.google.gson.*;
import java.util.Date;

/**
 * @author Damian Arellanes
 */
public abstract class DXManConnectorInstance {
    
  private final String id;
  private final String name;
  private final DXManServiceTemplate managedService;
  private final DXManConnectorRequester requester;

  protected final Gson gson;

  public DXManConnectorInstance(DXManServiceTemplate managedService, String name,
    DXManConnectorRequester requester, Gson gson) {

    this.id = managedService.getId();
    this.name = name;
    this.managedService = managedService;
    this.requester = requester;
    this.gson = gson;
  }
  
  public DXManConnectorInstance(String id, String name, 
    DXManConnectorRequester requester, Gson gson) {

    this.id = id;
    this.name = name;
    this.managedService = null;
    this.requester = requester;
    this.gson = gson;
  }

  public void init(String workflowJSON) {
    
    before();
    activate(workflowJSON);
    after();
  }

  private void before() {
     
    if(managedService != null) {
      System.out.println(managedService.getInfo().getName() 
        + " (" + this.getClass().getSimpleName()  + ") activated [" + new Date() + "]");
    }
    
    // Leave trace
  }

  private void after() {
      // TODO 
  }

  public abstract void activate(String workflowJSON);
  
  public void transferControl(DXManWfNode subworkflow, String uri) {        
    requester.transferControl(subworkflow, uri);
  }

  public String getId() { return id; }
  public String getName() { return name; }
  public DXManServiceTemplate getManagedService() { return managedService; }
}
