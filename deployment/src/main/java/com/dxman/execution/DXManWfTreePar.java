package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfTreePar extends DXManWfTree {
  
  public DXManWfTreePar(String id, String uri) {    
    super(new DXManWfParallel(id, uri));
  }
  
  protected void composeWf(DXManWfTree subWfTree, int tasks) {    
    composeWf(subWfTree, new DXManWfParallelCustom(tasks));
  }

  @Override
  public DXManWfSpec build() {    
    
    design();
    return new DXManWfSpec(getWfNode().getId()+"-wf-spec", getWfNode());
  }
}
