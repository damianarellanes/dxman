package com.dxman.design.connectors.composition;

import com.dxman.design.connectors.common.*;

/**
 * @author Damian Arellanes
 */
public class DXManParallelTemplate extends DXManCompositionConnectorTemplate {
  
  private DXManParallelType parallelType;
    
  public DXManParallelTemplate(DXManParallelType parallelType) {
    super(DXManConnectorType.PARALLEL);
    this.parallelType = parallelType;
  }    

  public DXManParallelType getParallelType() { return parallelType; }
  public void setParallelType(DXManParallelType parallelType) {
    this.parallelType = parallelType;
  }
}
