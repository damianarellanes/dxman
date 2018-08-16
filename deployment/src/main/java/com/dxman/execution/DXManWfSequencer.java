package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public class DXManWfSequencer extends DXManWfNode {
 
  private DXManWfNode[] subNodes;

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
    
    return !getSubnodeMappers().isEmpty() && subNodes.length > 1;
  }
  
  public void finishSequence(int sequenceLength) {
    
    DXManWfNode[] sortedSubNodes = new DXManWfNode[sequenceLength];
    getSubnodeMappers().forEach((subNodeMapper) -> {
      
      for(int index: ((DXManWfSequencerCustom)subNodeMapper.getCustom()).getOrder()) {
        sortedSubNodes[index] = subNodeMapper.getNode();
      }
    });
    
    this.subNodes = sortedSubNodes;
  }
  
  public DXManWfNode[] getSubnodes() { return subNodes; }
  public void setSubnodes(DXManWfNode[] subNodes) { 
    this.subNodes = subNodes; 
  }
}
