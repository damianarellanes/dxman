package com.dxman.thing.deployment.common;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.*;
import com.dxman.thing.deployment.connectors.atomic.DXManInvocationInstance;
import com.dxman.thing.deployment.connectors.common.DXManConnectorInstance;
import com.dxman.thing.deployment.connectors.composite.DXManParallelInstance;
import com.dxman.thing.deployment.connectors.composite.DXManSequencerInstance;
import com.dxman.thing.server.base.DXManConnectorRequester;
import com.dxman.thing.server.base.DXManConnectorRequesterFactory;
import com.dxman.thing.server.base.DXManServer;
import com.dxman.utils.RuntimeTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
      gson, dataSpace, thingId
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
    RuntimeTypeAdapterFactory<DXManWfNode> adapter1 = RuntimeTypeAdapterFactory
      .of(DXManWfNode.class, "wfnode")
      .registerSubtype(DXManWfParallel.class, "wfparallel")
      .registerSubtype(DXManWfSequencer.class, "wfsequencer")
      //.registerSubtype(WfGuard.class, "wfguard")
      .registerSubtype(DXManWfInvocation.class, "wfinvocation");
    RuntimeTypeAdapterFactory<DXManWfNodeCustom> adapter2 = RuntimeTypeAdapterFactory
      .of(DXManWfNodeCustom.class, "wfnodecustom")
      .registerSubtype(DXManWfParallelCustom.class, "wfparallelcustom")
      .registerSubtype(DXManWfSequencerCustom.class, "wfsequencercustom");    
    
    return new GsonBuilder().disableHtmlEscaping()
      .registerTypeAdapterFactory(adapter1).registerTypeAdapterFactory(adapter2)
      .create();
  }
}
