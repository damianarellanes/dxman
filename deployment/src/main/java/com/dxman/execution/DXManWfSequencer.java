package com.dxman.execution;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Damian Arellanes
 */
public class DXManWfSequencer extends DXManWfNode {
 
  private List<DXManWfNode> subNodes;

  public DXManWfSequencer() {}

  public DXManWfSequencer(String id, String uri) {
    super(id, uri);
    subNodes = new ArrayList<>();
  }
  
  @Override
  public boolean isValid() {
    
    for(DXManWfNode subNode: subNodes) {
      if(!subNode.isValid()) return false;
    }
    
    return !subNodes.isEmpty();    
  }

  public List<DXManWfNode> getSubnodes() { return subNodes; }
  public void setSubnodes(List<DXManWfNode> subNodes) { this.subNodes = subNodes; }
}
