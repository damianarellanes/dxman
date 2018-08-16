package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public class DXManWfSequencerCustom extends DXManWfNodeCustom {
  
  private int[] order;
  
  public DXManWfSequencerCustom() {}

  public DXManWfSequencerCustom(int[] order) {
    this.order = order;
  }

  public int[] getOrder() { return order; }
  public void setOrder(int[] order) { this.order = order; }
}
