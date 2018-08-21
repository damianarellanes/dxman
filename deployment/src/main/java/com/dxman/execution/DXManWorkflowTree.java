package com.dxman.execution;

import com.dxman.utils.*;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWorkflowTree extends  DXManMap<String, DXManWfNode> {
  
  public void customiseOrder(String parentKey, String childKey, int... order) {    
    
    get(parentKey).customiseOrder(childKey, new DXManWfSequencerCustom(order));        
    ((DXManWfSequencer)get(parentKey)).increaseSequenceBy(order.length);
  }    
  
  public abstract void design();
  public abstract DXManWfInputs getInputs();
  public abstract DXManWfOutputs getOutputs();
}
