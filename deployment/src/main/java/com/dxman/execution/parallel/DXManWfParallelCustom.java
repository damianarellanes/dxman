package com.dxman.execution.parallel;

import com.dxman.execution.common.DXManWfNodeCustom;

/**
 * @author Damian Arellanes
 */
public class DXManWfParallelCustom extends DXManWfNodeCustom {   
  
  private int tasks = 0;
  
  public DXManWfParallelCustom() {}

  public DXManWfParallelCustom(int tasks) {
    this.tasks = tasks;
  }

  public int getTasks() { return tasks; }
  public void setTasks(int tasks) { this.tasks = tasks; }
}
