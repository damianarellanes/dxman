package com.dxman.execution.common;

/**
 * @author Damian Arellanes
 */
public class DXManWfNodeMapper {

  private DXManWfNode node;
  private DXManWfNodeCustom custom;
  
  public DXManWfNodeMapper() {}

  public DXManWfNodeMapper(DXManWfNode node, DXManWfNodeCustom custom) {
    this.node = node;
    this.custom = custom;
  }
  
  public DXManWfNode getNode() { return node; }
  public void setNode(DXManWfNode node) { this.node = node; }
  public DXManWfNodeCustom getCustom() { return custom; }
  public void setCustom(DXManWfNodeCustom custom) { this.custom = custom; }
}
