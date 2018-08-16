package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfTreePar extends DXManWfTree {
  
  public DXManWfTreePar(String id, String uri) {    
    super(id, uri, new DXManWfParallel(id, uri));
  }
  
  protected void composeWf(DXManWfTree wfTree, int tasks) {    
    composeWf(wfTree, new DXManWfParallelCustom(tasks));
  }

  @Override
  public DXManWfSpec build() {    
    
    design();
    return new DXManWfSpec(getId()+"-wf-spec", getWfNode());
  }
}
