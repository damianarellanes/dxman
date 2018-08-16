package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfTree {
  
  private final String id;
  private final String uri;
  private final DXManWfNode wfNode;

  public DXManWfTree(String id, String uri, DXManWfNode wfNode) {
    
    this.id = id;
    this.uri = uri;
    this.wfNode = wfNode;
  }
    
  protected void composeWf(DXManWfTree wfTree, DXManWfNodeCustom custom) {
   
    wfTree.build();    
    wfNode.getSubnodeMappers().add(new DXManWfNodeMapper(wfTree.getWfNode(), custom));
  }
  
  public String getId() { return id; }
  public String getUri() { return uri; }
  public DXManWfNode getWfNode() { return wfNode; }
    
  public abstract void design();
  public abstract DXManWfSpec build();
  public abstract DXManWfInputs getInputs();
  public abstract DXManWfOutputs getOutputs();
}
