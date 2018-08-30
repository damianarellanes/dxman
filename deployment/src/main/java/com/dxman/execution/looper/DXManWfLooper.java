package com.dxman.execution.looper;

import com.dxman.execution.common.*;

/**
 * @author Damian Arellanes
 */
public class DXManWfLooper extends DXManWfNode {
    
  public DXManWfLooper() {}

  public DXManWfLooper(String id, String uri) {
    super(id, uri);
  }
  
  @Override
  public boolean isValid() {    
    return true;
  }
  
  @Override
  public DXManWfSpec build() {
    
    for(DXManWfNodeMapper subNodeMapper: getSubnodeMappers()) {
      
      if(subNodeMapper.getCustom() == null) 
        getSubnodeMappers().remove(subNodeMapper);
    }
    
    return new DXManWfSpec(getId()+"-wf-spec", this);
  }    
}
