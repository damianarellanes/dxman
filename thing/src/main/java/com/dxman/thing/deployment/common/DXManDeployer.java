package com.dxman.thing.deployment.common;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.thing.deployment.connectors.atomic.DXManInvocationInstance;
import com.dxman.thing.server.base.DXManServer;

/**
 * @author Damian Arellanes
 */
public class DXManDeployer {
  
  private final DXManServer server;
  private final DXManDataSpace dataSpace;
  private final String thingId;
  
  public DXManDeployer(DXManServer server, DXManDataSpace dataSpace, 
    String thingId) {
    
    this.server = server;
    this.dataSpace = dataSpace;
    this.thingId = thingId;
  }
  
  public String deployAtomicService(DXManAtomicServiceTemplate managedService) {
    
    DXManInvocationInstance ic = new DXManInvocationInstance(
      managedService, dataSpace, thingId
    );    
    
    server.deploy(ic);
    
    return ic.getId();
  }
    
}
