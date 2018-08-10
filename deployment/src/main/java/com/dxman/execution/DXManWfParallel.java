package com.dxman.execution;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Damian Arellanes
 */
public class DXManWfParallel extends DXManWfNode {
  
  private List<DXManWfNodeMapper> subNodeMappers;
  
  public DXManWfParallel() {}

  public DXManWfParallel(String id, String uri) {
    super(id, uri);
    subNodeMappers = new ArrayList<>();
  }

  @Override
  public boolean isValid() {
    
    for(DXManWfNodeMapper subNodeMapper: subNodeMappers) {      
      
      if(subNodeMapper.getNode() == null || !subNodeMapper.getNode().isValid()
        || subNodeMapper.getCustom() == null 
        || !subNodeMapper.getCustom().getClass().equals(DXManWfParallelCustom.class)
        || ((DXManWfParallelCustom)subNodeMapper.getCustom()).getTasks() <= 0
      ) return false;
    }
    
    return !subNodeMappers.isEmpty();
  }
    
  public List<DXManWfNodeMapper> getSubnodeMappers() { return subNodeMappers; }
  public void setSubnodeMappers(List<DXManWfNodeMapper> subNodeMappers) { 
    this.subNodeMappers = subNodeMappers; 
  }
}
