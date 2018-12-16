package com.dxman.thing.deployment.common;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.dataspace.base.DXManDataSpaceFactory;
import com.dxman.deployment.common.DXManDeploymentUtils;
import com.dxman.design.connectors.common.DXManConnectorTemplate;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.thing.deployment.connectors.adapters.DXManGuardInstance;
import com.dxman.thing.deployment.connectors.adapters.DXManLooperInstance;
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
  private final String dataspaceEndpoint;
  
  public DXManDeployer(DXManServer server, String dataspaceEndpoint) {    
    this.server = server;
    this.dataspaceEndpoint = dataspaceEndpoint;    
  }
  
  public String deployAtomicService(DXManAtomicServiceTemplate managedService) {
    
    // TODO Create a unique solution to swicth between Coap, Rest, events, etc
    Gson gson = DXManDeploymentUtils.buildSerializationGson();
    DXManInvocationInstance ic = new DXManInvocationInstance(
      managedService, DXManConnectorRequesterFactory.createCoapRequester(gson), 
      gson, 
      new DXManInvocationDataManager(
        managedService.getOperations(),
        DXManDataSpaceFactory.createBlockchainManager(dataspaceEndpoint)
      )
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
          managedService, new DXManConnectorDataManager(
            DXManDataSpaceFactory.createBlockchainManager(dataspaceEndpoint)
          ), 
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
  
  public String deployAdapter(DXManConnectorTemplate adapter) {
    
    Gson gson = DXManDeploymentUtils.buildSerializationGson();
    DXManConnectorRequester connectorRequester 
      = DXManConnectorRequesterFactory.createCoapRequester(gson);
    
    DXManConnectorInstance connectorInstance = null;    
    switch(adapter.getType()) {
      
      case GUARD:
        connectorInstance = new DXManGuardInstance(
          adapter.getId(), adapter.getName(), 
          new DXManConnectorDataManager(
            DXManDataSpaceFactory.createBlockchainManager(dataspaceEndpoint)
          ), connectorRequester, gson);
        break;
      case LOOPER:
        connectorInstance = new DXManLooperInstance(
          adapter.getId(), adapter.getName(), 
          new DXManConnectorDataManager(
            DXManDataSpaceFactory.createBlockchainManager(dataspaceEndpoint)
          ), connectorRequester, gson);
        break;
    }
    
    server.deploy(connectorInstance);
    
    return connectorInstance.getId();
  }
}
