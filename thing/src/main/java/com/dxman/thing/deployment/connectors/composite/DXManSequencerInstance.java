package com.dxman.thing.deployment.connectors.composite;

import com.dxman.execution.sequencer.DXManWfSequencer;
import com.dxman.execution.common.DXManWfNode;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.thing.deployment.connectors.common.DXManConnectorInstance;
import com.dxman.thing.server.base.DXManConnectorRequester;
import com.google.gson.Gson;
import java.util.Date;

/**
 * @author Damian Arellanes
 */
public class DXManSequencerInstance extends DXManConnectorInstance {
  
  public DXManSequencerInstance(DXManCompositeServiceTemplate managedService, 
    DXManConnectorRequester requester, Gson gson) {
    
    super(managedService, managedService.getConnector().getName(),
      requester, gson);
  }

  @Override
  public void activate(String workflowJSON) {
    
    DXManWfSequencer flow = gson.fromJson(workflowJSON, DXManWfSequencer.class);
    
    for(DXManWfNode subNode: flow.getSequence()) {
      transferControl(subNode, subNode.getUri());
    }
  }    
}
