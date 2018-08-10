package com.dxman.thing.deployment.connectors.composite;

import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.*;
import com.dxman.thing.deployment.connectors.common.DXManConnectorInstance;
import com.dxman.thing.server.base.DXManConnectorRequester;
import com.google.gson.Gson;

/**
 * @author Damian Arellanes
 */
public class DXManSequencerInstance extends DXManConnectorInstance {
  
  //private final List<DXManServiceTemplate> subServices = new ArrayList<>();

  public DXManSequencerInstance(DXManCompositeServiceTemplate managedService, 
    DXManConnectorRequester requester, Gson gson) {
    
    super(managedService, requester, gson);
  }
  
  /*public void composeServices(DXManServiceTemplate... services) {
    subServices.addAll(Arrays.asList(services));
  }*/

  @Override
  public void activate(String workflowJSON) {
    
    System.err.println("Sequencer connector activated!");
    
    DXManWfSequencer flow = gson.fromJson(workflowJSON, DXManWfSequencer.class);
    
    for(DXManWfNode subNode: flow.getSubnodes()) {
      transferControl(subNode, subNode.getUri());
    }
  }    
}
