package com.dxman.deployment.cli;

import com.dxman.execution.wttree.*;
import com.dxman.execution.sequencer.*;
import com.dxman.execution.parallel.DXManWfParallelCustom;
import com.dxman.design.data.*;
import com.dxman.execution.common.*;
import com.dxman.execution.guard.DXManWfGuardCustom;
import com.dxman.execution.looper.DXManWfLooperCustomDyn;
import com.dxman.execution.looper.DXManWfLooperCustomStatic;
import com.dxman.execution.selector.DXManWfSelectorCustom;

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
  
  public void customiseParallel(String parentKey, String childKey, int tasks) {    
    
    workflowTree.getWt().get(parentKey).customise(
      childKey, new DXManWfParallelCustom(tasks)
    );
  }
  
  public void customiseSelector(String parentKey, String childKey, 
    String parameterId, DXManWfConditionOperator operator, String value) {
    
    workflowTree.getWt().get(parentKey).customise(
      childKey, 
      new DXManWfSelectorCustom(
        new DXManWfCondition(parameterId, operator, value)
      )
    );
  }
  
  public void customiseGuard(String parentKey, String childKey, 
    String parameterId, DXManWfConditionOperator operator, String value) {
    
    workflowTree.getWt().get(parentKey).customise(
      childKey, 
      new DXManWfGuardCustom(
        new DXManWfCondition(parameterId, operator, value)
      )
    );
  }
  
  public void customiseLooperDyn(String parentKey, String childKey, 
    String parameterId, DXManWfConditionOperator operator, String value) {
    
    workflowTree.getWt().get(parentKey).customise(
      childKey, 
      new DXManWfLooperCustomDyn(
        new DXManWfCondition(parameterId, operator, value)
      )
    );
  }
  public void customiseLooperStatic(String parentKey, String childKey, 
    int iterations) {
    
    workflowTree.getWt().get(parentKey).customise(
      childKey, new DXManWfLooperCustomStatic(iterations)
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
