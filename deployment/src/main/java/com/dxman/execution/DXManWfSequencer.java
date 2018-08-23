package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public class DXManWfSequencer extends DXManWfNode {
 
  private DXManWfNode[] sequence;
  private int sequenceLength = 0;

  public DXManWfSequencer() {}

  public DXManWfSequencer(String id, String uri, String workflowId) {
    super(id, uri, workflowId);
  }
  
  public void increaseSequenceBy(int addition) {
    setSequenceLength(sequenceLength + addition);
  }
  
  public void finishSequence() {
    
    setSequence(new DXManWfNode[sequenceLength]);
    for(DXManWfNodeMapper subNodeMapper: getSubnodeMappers()) {
      
      if(subNodeMapper.getCustom() != null) {
        for(int index: ((DXManWfSequencerCustom)subNodeMapper.getCustom()).getOrder()) {
          sequence[index] = subNodeMapper.getNode();
        } 
      }
    }    
  }
  
  public DXManWfNode[] getSequence() { return sequence; }
  public void setSequence(DXManWfNode[] sequence) { this.sequence = sequence; }
  
  public int getSequenceLength() { return sequenceLength; }
  public void setSequenceLength(int sequenceLength) {
    this.sequenceLength = sequenceLength;
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
  
  @Override
  public DXManWfSpec build() {
    
    DXManWfSpec wfSpec = new DXManWfSpec(getId()+"-wf-spec", this);
    finishSequence();
    
    return wfSpec;
  }
}
