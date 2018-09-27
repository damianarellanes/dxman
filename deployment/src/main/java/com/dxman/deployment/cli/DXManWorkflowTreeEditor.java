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
import com.dxman.utils.DXManIDGenerator;
import java.util.ArrayList;

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
    
    if(destination.isDataProcessor()) {      
      destination.getDataProcessor().addWriter(
        DXManIDGenerator.generateParameterUUID(origin.getDataEntityId(), workflowTree.getId())
      );
    }
    
    workflowTree.getDataChannels().get(parentKey).add(
      new DXManDataChannel(origin, destination)
    );
  }
  
  public DXManMapperTemplate addMapper(String name, String processsorPath, 
      DXManDataProcessorLang processorLang) {    
    
    DXManMapperTemplate mapper = new DXManMapperTemplate(
      name, processsorPath, processorLang
    );
    workflowTree.getDataProcessors().add(mapper);
    
    return mapper;
  }
  
  public DXManReducerTemplate addReducer(String name, String processsorPath, 
    DXManDataProcessorLang processorLang) {    
    
    DXManReducerTemplate reducer = new DXManReducerTemplate(
      name, processsorPath, processorLang
    );
    workflowTree.getDataProcessors().add(reducer);
    
    return reducer;
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
