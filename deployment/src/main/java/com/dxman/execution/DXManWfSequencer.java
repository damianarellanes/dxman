package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public class DXManWfSequencer extends DXManWfNode {
 
  private DXManWfNode[] sequence;

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
  
  public void finishSequence(int sequenceLength) {
    
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
}
