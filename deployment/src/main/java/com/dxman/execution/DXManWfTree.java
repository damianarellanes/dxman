package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfTree {
  
  private final DXManWfNode wfNode;

  public DXManWfTree(DXManWfNode wfNode) {
    this.wfNode = wfNode;
  }
    
  protected void composeWf(DXManWfTree wfTree, DXManWfNodeCustom custom) {
   
    wfTree.build();    
    wfNode.getSubnodeMappers().add(new DXManWfNodeMapper(wfTree.getWfNode(), custom));
  }
  
  public DXManWfNode getWfNode() { return wfNode; }
    
  public abstract void design();
  public abstract DXManWfSpec build();
  public abstract DXManWfInputs getInputs();
  public abstract DXManWfOutputs getOutputs();
}
