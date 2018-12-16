package com.dxman.execution.invocation;

import com.dxman.execution.common.DXManWfSpec;
import com.dxman.execution.common.DXManWfNode;

/**
 * @author Damian Arellanes
 */
public class DXManWfInvocation extends DXManWfNode {  

  public DXManWfInvocation(String id, String uri, String operationName) {
    super(id, uri);
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
