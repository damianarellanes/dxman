package com.dxman.design.connectors.composition;

import com.dxman.design.connectors.common.DXManConnectorTemplate;
import com.dxman.design.connectors.common.DXManConnectorType;
import com.dxman.design.distribution.DXManDeploymentInfo;

/**
 * @author Damian Arellanes
 */
public class DXManSelectorTemplate extends DXManConnectorTemplate {
    
  public DXManSelectorTemplate(String name, DXManDeploymentInfo deploymentInfo) {
    super(name, DXManConnectorType.SELECTOR, deploymentInfo);
  }

  @Override
  public String toString() {
    return "Selector " + getName() +" inputs: " + getInputs().values() ;
  }
}
