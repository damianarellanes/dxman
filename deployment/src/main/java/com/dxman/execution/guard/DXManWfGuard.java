package com.dxman.execution.guard;

import com.dxman.execution.common.DXManWfSpec;
import com.dxman.execution.common.DXManWfNodeMapper;
import com.dxman.execution.common.DXManWfNode;
import com.dxman.execution.selector.*;

/**
 * @author Damian Arellanes
 */
public class DXManWfGuard extends DXManWfNode {
    
  public DXManWfGuard() {}

  public DXManWfGuard(String id, String uri) {
    super(id, uri);
  }
  
  @Override
  public boolean isValid() {
    
    for(DXManWfNodeMapper subNodeMapper: getSubnodeMappers()) {      
      
      if(subNodeMapper.getNode() == null || !subNodeMapper.getNode().isValid()
        || subNodeMapper.getCustom() == null 
        || !subNodeMapper.getCustom().getClass().equals(DXManWfSelectorCustom.class)
        || ((DXManWfGuardCustom)subNodeMapper.getCustom()).getCondition() == null
        || ((DXManWfGuardCustom)subNodeMapper.getCustom()).getCondition().getOperator() == null
      ) return false;
    }
    
    return !getSubnodeMappers().isEmpty();
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
