package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfTreeSeq extends DXManWfTree {
  
  private int sequenceLength;

  public DXManWfTreeSeq(String id, String uri) {
    
    super(new DXManWfSequencer(id, uri));    
    sequenceLength = 0;
  }
  
  protected void composeWf(DXManWfTree subWfTree, int... order) {
    
    composeWf(subWfTree, new DXManWfSequencerCustom(order));
    sequenceLength += order.length;
  }

  @Override
  public DXManWfSpec build() {
    
    design();
    
    DXManWfSpec wfSpec = new DXManWfSpec(getWfNode().getId()+"-wf-spec", getWfNode());
    ((DXManWfSequencer)getWfNode()).finishSequence(sequenceLength);
    
    return wfSpec;
  }
}
