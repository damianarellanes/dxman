package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfTreeSeq extends DXManWfTree {
  
  public DXManWfTreeSeq(String id, String uri) {    
    super(new DXManWfSequencer(id, uri));
  }
  
  protected void composeWf(DXManWfTree subWfTree, int... order) {
    
    composeWf(subWfTree, new DXManWfSequencerCustom(order));
    ((DXManWfSequencer)getWfNode()).increaseSequenceBy(order.length);
  }

  @Override
  public DXManWfSpec build() {
    
    design();
    
    DXManWfSpec wfSpec = new DXManWfSpec(getWfNode().getId()+"-wf-spec", getWfNode());
    ((DXManWfSequencer)getWfNode()).finishSequence();
    
    return wfSpec;
  }
}
