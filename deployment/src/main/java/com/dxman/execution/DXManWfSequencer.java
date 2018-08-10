package com.dxman.execution;

import java.util.*;

/**
 * @author Damian Arellanes
 */
public class DXManWfSequencer extends DXManWfNode {
 
  private List<DXManWfNodeMapper> subNodeMappers;
  private DXManWfNode[] subNodes;

  public DXManWfSequencer() {}

  public DXManWfSequencer(String id, String uri) {
    super(id, uri);
    subNodeMappers = new ArrayList<>();
  }
  
  @Override
  public boolean isValid() {
    
    if (!subNodeMappers.stream().noneMatch((subNodeMapper) -> 
      (subNodeMapper.getNode() == null || !subNodeMapper.getNode().isValid()
      || subNodeMapper.getCustom() == null
      || !subNodeMapper.getCustom().getClass().equals(DXManWfSequencerCustom.class)
      || ((DXManWfSequencerCustom)subNodeMapper.getCustom()).getOrder().isEmpty()
      ))) { return false; }
    
    return !subNodeMappers.isEmpty() && subNodes.length > 1;
  }
  
  public void finishSequence() {
    
    List<Integer> sequence = new ArrayList<>();    
    subNodeMappers.forEach((subNodeMapper) -> {
      sequence.addAll(((DXManWfSequencerCustom)subNodeMapper.getCustom()).getOrder());
    });
    int max = Collections.max(sequence) + 1;

    DXManWfNode[] sortedSubNodes = new DXManWfNode[max];
    subNodeMappers.forEach((subNodeMapper) -> {
      
      ((DXManWfSequencerCustom)subNodeMapper.getCustom()).getOrder()
        .forEach((order) -> {
          sortedSubNodes[order] = subNodeMapper.getNode();
        });
    });
    
    this.subNodes = sortedSubNodes;
  }

  public List<DXManWfNodeMapper> getSubnodeMappers() { return subNodeMappers; }
  public void setSubnodeMappers(List<DXManWfNodeMapper> subNodeMappers) { 
    this.subNodeMappers = subNodeMappers; 
  }
  public DXManWfNode[] getSubnodes() { return subNodes; }
  public void setSubnodes(DXManWfNode[] subNodes) { 
    this.subNodes = subNodes; 
  }
}
