package com.dxman.deployment.cli;

import com.dxman.design.data.*;
import com.dxman.execution.*;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWorkflowTreeEditor {
  
  private final DXManWorkflowTree workflowTree;
  
  public DXManWorkflowTreeEditor(DXManWorkflowTree workflowTree, String wfId) {
    this.workflowTree = workflowTree;
    this.workflowTree.setId(wfId);
  }
  
  public void customiseOrder(String parentKey, String childKey, int... order) {    
    
    workflowTree.getWt().get(parentKey).customise(
      childKey, new DXManWfSequencerCustom(order)
    );
    
    ((DXManWfSequencer)workflowTree.getWt().get(parentKey))
      .increaseSequenceBy(order.length);
  }
  
  private void customiseParallel(String parentKey, String childKey, int tasks) {    
    
    workflowTree.getWt().get(parentKey).customise(
      childKey, new DXManWfParallelCustom(tasks)
    );
  }
  
  public void addDataChannel(String parentKey, DXManDataChannelPoint origin, 
    DXManDataChannelPoint destination) {
    
    workflowTree.getWt().get(parentKey).getDataChannels().add(
      new DXManDataChannel(origin, destination)
    );
  }
  
  public void design() {
    designControl();
    designData();
  }  
  
  public DXManWorkflowTree getWorkflowTree() { return workflowTree; }
  
  public abstract void designControl();  
  public abstract void designData();
  public abstract DXManWfInputs getInputs();
  public abstract DXManWfOutputs getOutputs();
}
