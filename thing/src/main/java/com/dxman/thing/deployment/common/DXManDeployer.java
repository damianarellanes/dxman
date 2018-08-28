package com.dxman.thing.deployment.common;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.*;
import com.dxman.execution.selector.*;
import com.dxman.thing.deployment.connectors.atomic.*;
import com.dxman.thing.deployment.connectors.common.*;
import com.dxman.thing.deployment.connectors.composite.*;
import com.dxman.thing.server.base.*;
import com.dxman.utils.RuntimeTypeAdapterFactory;
import com.google.gson.*;

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
    
    // TODO Create a unique solution to swicth between Coap, Rest, events, etc
    Gson gson = createGson();
    DXManInvocationInstance ic = new DXManInvocationInstance(
      managedService, DXManConnectorRequesterFactory.createCoapRequester(gson), 
      gson, 
      new DXManInvocationDataManager(dataSpace, managedService.getOperations()), 
      thingId
    );    
    
    server.deploy(ic);
    
    return ic.getId();
  }
   
  public String deployCompositeService(DXManCompositeServiceTemplate managedService) {
    
    Gson gson = createGson();    
    DXManConnectorRequester connectorRequester 
      = DXManConnectorRequesterFactory.createCoapRequester(gson);
    
    DXManConnectorInstance connectorInstance = null;    
    switch(managedService.getCompositionConnector().getType()) {
      
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
  
  private Gson createGson () {
    
    // TODO Create a unique solution to swicth between Coap, Rest, events, etc
    
    RuntimeTypeAdapterFactory<DXManWfNode> adapter2 = RuntimeTypeAdapterFactory
      .of(DXManWfNode.class)
      .registerSubtype(DXManWfParallel.class)
      .registerSubtype(DXManWfSelector.class)
      .registerSubtype(DXManWfSequencer.class)
      .registerSubtype(DXManWfInvocation.class);
    
    RuntimeTypeAdapterFactory<DXManWfNodeCustom> adapter3 = RuntimeTypeAdapterFactory
      .of(DXManWfNodeCustom.class)
      .registerSubtype(DXManWfParallelCustom.class)
      .registerSubtype(DXManWfSelectorCustom.class)
      .registerSubtype(DXManWfSequencerCustom.class);
    
    return new GsonBuilder().disableHtmlEscaping()
      .registerTypeAdapterFactory(adapter2)
      .registerTypeAdapterFactory(adapter3)
      .create();
  }
}
