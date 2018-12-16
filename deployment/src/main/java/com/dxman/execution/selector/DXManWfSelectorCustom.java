package com.dxman.execution.selector;

import com.dxman.execution.common.DXManWfCondition;
import com.dxman.execution.common.DXManWfNodeCustom;

/**
 * @author Damian Arellanes
 */
public class DXManWfSelectorCustom extends DXManWfNodeCustom {   
  
  private DXManWfCondition condition;
  
  public DXManWfSelectorCustom() {}

  public DXManWfSelectorCustom(DXManWfCondition condition) {
    this.condition = condition;
  }

  public DXManWfCondition getCondition() { return condition; }
  public void setCondition(DXManWfCondition condition) {
    this.condition = condition;
  }
}
