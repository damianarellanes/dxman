package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public class DXManWfSequencer extends DXManWfNode {
 
  private DXManWfNode[] sequence;
  private int sequenceLength = 0;

  public DXManWfSequencer() {}

  public DXManWfSequencer(String id, String uri) {
    super(id, uri);    
  }
  
  @Override
  public boolean isValid() {
    
    if (!getSubnodeMappers().stream().noneMatch((subNodeMapper) -> 
      (subNodeMapper.getNode() == null || !subNodeMapper.getNode().isValid()
      || subNodeMapper.getCustom() == null
      || !subNodeMapper.getCustom().getClass().equals(DXManWfSequencerCustom.class)
      || ((DXManWfSequencerCustom)subNodeMapper.getCustom()).getOrder().length == 0
      ))) { return false; }
    
    return !getSubnodeMappers().isEmpty() && sequence.length > 1;
  }
  
  protected void composeWf(DXManWfNode subWfNode, int... order) {
    
    composeWf(subWfNode, new DXManWfSequencerCustom(order));
    increaseSequenceBy(order.length);
  }
  
  public void increaseSequenceBy(int addition) {
    sequenceLength += addition;
  }
  
  public void finishSequence() {
    
    sequence = new DXManWfNode[sequenceLength];
    getSubnodeMappers().forEach((subNodeMapper) -> {
      
      for(int index: ((DXManWfSequencerCustom)subNodeMapper.getCustom()).getOrder()) {
        sequence[index] = subNodeMapper.getNode();
      }
    });
  }
  
  public DXManWfNode[] getSequence() { return sequence; }
  public void setSequence(DXManWfNode[] subNodes) { 
    this.sequence = subNodes; 
  }
  
  @Override
  public DXManWfSpec build() {
    
    DXManWfSpec wfSpec = new DXManWfSpec(getId()+"-wf-spec", this);
    finishSequence();
    
    return wfSpec;
  }
}
