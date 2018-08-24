package com.dxman.thing.deployment.connectors.common;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.execution.selector.DXManWfCondition;

/**
 * @author Damian Arellanes
 */
public class DXManConnectorDataManager {
    
  private final DXManDataSpace dataSpace;

  public DXManConnectorDataManager(DXManDataSpace dataSpace) {
    this.dataSpace = dataSpace;
  }
  
  public boolean matches(String wfId, DXManWfCondition condition) {
        
    String dataSpaceVal = dataSpace.readParameter(
      condition.getParameterId(), wfId
    );
    
    switch(condition.getOperator()) {
      case EQUAL:
        return condition.getValue().equals(dataSpaceVal);
      case NOT_EQUAL:
        return !condition.getValue().equals(dataSpaceVal);
    }
    return false;
  }
}
