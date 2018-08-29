package com.dxman.design.connectors.composition;

import com.dxman.design.connectors.common.*;
import com.dxman.design.distribution.DXManDeploymentInfo;

/**
 * @author Damian Arellanes
 */
public class DXManParallelTemplate extends DXManConnectorTemplate {
  
  private DXManParallelType parallelType;
    
  public DXManParallelTemplate(String name, DXManParallelType parallelType, 
    DXManDeploymentInfo deploymentInfo) {
    super(name, DXManConnectorType.PARALLEL, deploymentInfo);
    this.parallelType = parallelType;
  }    

  public DXManParallelType getParallelType() { return parallelType; }
  public void setParallelType(DXManParallelType parallelType) {
    this.parallelType = parallelType;
  }
}
