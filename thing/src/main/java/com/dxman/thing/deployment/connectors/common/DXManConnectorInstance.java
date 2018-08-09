package com.dxman.thing.deployment.connectors.common;


import com.dxman.design.services.common.DXManServiceTemplate;
import com.dxman.execution.*;
import com.dxman.thing.server.base.DXManConnectorRequester;
import com.dxman.utils.*;
import com.google.gson.*;

/**
 * @author Damian Arellanes
 */
public abstract class DXManConnectorInstance {
    
  private final String id;
  private final DXManServiceTemplate managedService;
  private final DXManConnectorRequester requester;

  protected final Gson gson;

  public DXManConnectorInstance(DXManServiceTemplate managedService, 
    DXManConnectorRequester requester, Gson gson) {

    //this.id = managedService.getInfo().getName();
    this.id = DXManIDGenerator.generateConnectorID();
    this.managedService = managedService;
    this.requester = requester;
    this.gson = gson;
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
  
  public void transferControl(DXManWfNode subworkflow, String uri) {        
    requester.transferControl(subworkflow, uri);
  }

  public String getId() { return id; }
  public DXManServiceTemplate getManagedService() { return managedService; }
}
