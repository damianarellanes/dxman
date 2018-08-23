package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public class DXManWfParallel extends DXManWfNode {
  
  public DXManWfParallel() {}

  public DXManWfParallel(String id, String uri) {
    super(id, uri);
  }
  
  @Override
  public boolean isValid() {
    
    for(DXManWfNodeMapper subNodeMapper: getSubnodeMappers()) {      
      
      if(subNodeMapper.getNode() == null || !subNodeMapper.getNode().isValid()
        || subNodeMapper.getCustom() == null 
        || !subNodeMapper.getCustom().getClass().equals(DXManWfParallelCustom.class)
        || ((DXManWfParallelCustom)subNodeMapper.getCustom()).getTasks() <= 0
      ) return false;
    }
    
    return !getSubnodeMappers().isEmpty();
  }
  
  @Override
  public DXManWfSpec build() {
    return new DXManWfSpec(getId()+"-wf-spec", this);
  }
}
