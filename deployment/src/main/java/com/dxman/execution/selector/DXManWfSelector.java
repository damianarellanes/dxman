package com.dxman.execution.selector;

import com.dxman.execution.*;

/**
 * @author Damian Arellanes
 */
public class DXManWfSelector extends DXManWfNode {
    
  public DXManWfSelector() {}

  public DXManWfSelector(String id, String uri) {
    super(id, uri);
  }
  
  @Override
  public boolean isValid() {
    
    for(DXManWfNodeMapper subNodeMapper: getSubnodeMappers()) {      
      
      if(subNodeMapper.getNode() == null || !subNodeMapper.getNode().isValid()
        || subNodeMapper.getCustom() == null 
        || !subNodeMapper.getCustom().getClass().equals(DXManWfSelectorCustom.class)
        || !((DXManWfSelectorCustom)subNodeMapper.getCustom()).getCondition().getOperator().equals("==")
        || !((DXManWfSelectorCustom)subNodeMapper.getCustom()).getCondition().getOperator().equals("!=")
      ) return false;
    }
    
    return !getSubnodeMappers().isEmpty();
  }
  
  @Override
  public DXManWfSpec build() {
    return new DXManWfSpec(getId()+"-wf-spec", this);
  }
}
