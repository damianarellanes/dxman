package com.dxman.execution;

import java.util.ArrayList;

/**
 * @author Damian Arellanes
 */
public class DXManWfSequencerCustom extends DXManWfNodeCustom {
  
  private ArrayList<Integer> order;
  
  public DXManWfSequencerCustom() {}

  public DXManWfSequencerCustom(ArrayList<Integer> order) {
    this.order = order;
  }

  public ArrayList<Integer> getOrder() { return order; }
  public void setOrder(ArrayList<Integer> order) { this.order = order; }
}
