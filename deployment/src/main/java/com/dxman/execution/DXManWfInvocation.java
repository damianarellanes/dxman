package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public class DXManWfInvocation extends DXManWfNode {    
    
  public DXManWfInvocation(String id, String uri, String workflowId) {
    super(id, uri, workflowId);
  }
  
  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public DXManWfSpec build() {
    return new DXManWfSpec(getId()+"-wf-spec", this);
  }  
}
