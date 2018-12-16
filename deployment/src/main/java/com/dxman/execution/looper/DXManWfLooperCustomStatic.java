package com.dxman.execution.looper;

import com.dxman.execution.common.DXManWfNodeCustom;

/**
 * @author Damian Arellanes
 */
public class DXManWfLooperCustomStatic extends DXManWfNodeCustom {
  
  private int iterations;
  
  public DXManWfLooperCustomStatic() {}

  public DXManWfLooperCustomStatic(int iterations) {
    this.iterations = iterations;
  }    
  
  public int getIterations() { return iterations; }
  public void setIterations(int iterations) { this.iterations = iterations; }
}
