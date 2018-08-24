package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public class DXManWfInvocation extends DXManWfNode {  

  private String operationName; // TODO remove
    
  public DXManWfInvocation(String id, String uri, String operationName) {
    super(id, uri);
    this.operationName = operationName;
  }
  
  public String getOperationName() { return operationName; }
  public void setOperationName(String operationName) {
    this.operationName = operationName;
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
