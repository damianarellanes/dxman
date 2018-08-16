package com.dxman.execution;

import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfTreePar extends DXManWfTree {
  
  public DXManWfTreePar(String id, String uri, 
    DXManWfTree... subWorkflows) {
    
    super(id, uri, new DXManWfParallel(id, uri), subWorkflows);
  }
  
  protected void composeWf(String wfId, int taskNumber) {  
    composeWf(wfId, new DXManWfParallelCustom(taskNumber));
  }
  
  protected void composeWf(DXManWfInvocation inv, int taskNumber) {    
    composeWf(inv, new DXManWfParallelCustom(taskNumber));
  }

  @Override
  public DXManWfSpec build() {    
    
    design();
    return new DXManWfSpec(getId()+"-wf-spec", getWfNode());
  }
  
  @Override
  public DXManWfResult execute(DXManWfManager wfManager)
    throws JSONException {
    
    DXManWfSpec wfSpec = build();
        
    // Executes the workflow
    return wfManager.executeWorkflow(wfSpec, getInputs(), getOutputs());
  }
}
