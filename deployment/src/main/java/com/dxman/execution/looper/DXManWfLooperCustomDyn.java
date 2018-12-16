package com.dxman.execution.looper;

import com.dxman.execution.common.*;

/**
 * @author Damian Arellanes
 */
public class DXManWfLooperCustomDyn extends DXManWfNodeCustom {
    
  private DXManWfCondition condition;
  
  public DXManWfLooperCustomDyn() {}

  public DXManWfLooperCustomDyn(DXManWfCondition condition) {
    this.condition = condition;
  }

  public DXManWfCondition getCondition() { return condition; }
  public void setCondition(DXManWfCondition condition) {
    this.condition = condition;
  }
}
