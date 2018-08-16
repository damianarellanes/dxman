package com.dxman.execution;

import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfTreeSeq extends DXManWfTree {
  
  private int sequenceLength;

  public DXManWfTreeSeq(String id, String uri, 
    DXManWfTree... subWorkflows) {
    
    super(id, uri, new DXManWfSequencer(id, uri), subWorkflows);
    
    sequenceLength = 0;
  }
  
  protected void composeWf(String wfId, int... order) {
    
    composeWf(wfId, new DXManWfSequencerCustom(order));    
    sequenceLength += order.length;
  }
  
  protected void composeWf(DXManWfInvocation inv, int... order) {
    
    composeWf(inv, new DXManWfSequencerCustom(order));    
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
  public DXManWorkflowResult execute(DXManWorkflowManager wfManager)
    throws JSONException {
        
    DXManWfSpec wfSpec = build();
        
    // Executes the workflow
    return wfManager.executeWorkflow(wfSpec, getInputs(), getOutputs());
  }
}
