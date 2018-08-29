package com.dxman.thing.deployment.common;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.deployment.common.DXManDeploymentUtils;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.thing.deployment.connectors.atomic.*;
import com.dxman.thing.deployment.connectors.common.*;
import com.dxman.thing.deployment.connectors.composite.*;
import com.dxman.thing.server.base.*;
import com.google.gson.*;

/**
 * @author Damian Arellanes
 */
public class DXManDeployer {
  
  private final DXManServer server;
  private final DXManDataSpace dataSpace;
  
  public DXManDeployer(DXManServer server, DXManDataSpace dataSpace) {
    
    this.server = server;
    this.dataSpace = dataSpace;
  }
  
  public String deployAtomicService(DXManAtomicServiceTemplate managedService) {
    
    // TODO Create a unique solution to swicth between Coap, Rest, events, etc
    Gson gson = DXManDeploymentUtils.buildSerializationGson();
    DXManInvocationInstance ic = new DXManInvocationInstance(
      managedService, DXManConnectorRequesterFactory.createCoapRequester(gson), 
      gson, 
      new DXManInvocationDataManager(dataSpace, managedService.getOperations())
    );    
    
    server.deploy(ic);
    
    return ic.getId();
  }
   
  public String deployCompositeService(DXManCompositeServiceTemplate managedService) {
    
    Gson gson = DXManDeploymentUtils.buildSerializationGson();
    DXManConnectorRequester connectorRequester 
      = DXManConnectorRequesterFactory.createCoapRequester(gson);
    
    DXManConnectorInstance connectorInstance = null;    
    switch(managedService.getConnector().getType()) {
      
      case SEQUENCER:
        connectorInstance = new DXManSequencerInstance(
          managedService, connectorRequester, gson
        );
        break;
      case SELECTOR:
        connectorInstance = new DXManSelectorInstance(
          managedService, new DXManConnectorDataManager(dataSpace), 
          connectorRequester, gson
        );
        break;  
      case PARALLEL:
        connectorInstance = new DXManParallelInstance(
          managedService, connectorRequester, gson
        );
        break;  
    }
    
    server.deploy(connectorInstance);
    
    return connectorInstance.getId();
  }
}
