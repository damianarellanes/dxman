package com.dxman.execution;

import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfTreeSeq extends DXManWfTree {
  
  private int sequenceLength;

  public DXManWfTreeSeq(String id, String uri) {
    
    super(id, uri, new DXManWfSequencer(id, uri));    
    sequenceLength = 0;
  }
  
  protected void composeWf(DXManWfTree wfTree, int... order) {
    
    composeWf(wfTree, new DXManWfSequencerCustom(order));
    sequenceLength += order.length;
  }

  @Override
  public DXManWfSpec build() {
    
    design();
    
    DXManWfSpec wfSpec = new DXManWfSpec(getId()+"-wf-spec", getWfNode());
    ((DXManWfSequencer)getWfNode()).finishSequence(sequenceLength);
    
    return wfSpec;
  }
  
  @Override
  public DXManWfResult execute(DXManWfManager wfManager) throws JSONException {
        
    DXManWfSpec wfSpec = build();
        
    // Executes the workflow
    return wfManager.executeWorkflow(wfSpec, getInputs(), getOutputs());
  }
}
