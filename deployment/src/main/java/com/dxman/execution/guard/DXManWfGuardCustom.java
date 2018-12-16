package com.dxman.execution.guard;

import com.dxman.execution.common.DXManWfCondition;
import com.dxman.execution.common.DXManWfNodeCustom;

/**
 * @author Damian Arellanes
 */
public class DXManWfGuardCustom extends DXManWfNodeCustom {   
  
  private DXManWfCondition condition;
  
  public DXManWfGuardCustom() {}

  public DXManWfGuardCustom(DXManWfCondition condition) {
    this.condition = condition;
  }

  public DXManWfCondition getCondition() { return condition; }
  public void setCondition(DXManWfCondition condition) {
    this.condition = condition;
  }
}
