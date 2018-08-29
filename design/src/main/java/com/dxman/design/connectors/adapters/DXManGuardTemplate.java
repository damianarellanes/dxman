package com.dxman.design.connectors.adapters;

import com.dxman.design.connectors.common.*;
import com.dxman.design.distribution.DXManDeploymentInfo;

/**
 * @author Damian Arellanes
 */
public class DXManGuardTemplate extends DXManConnectorTemplate {
    
  public DXManGuardTemplate(String name, DXManDeploymentInfo deploymentInfo) {
    super(name, DXManConnectorType.GUARD, deploymentInfo);
  }

  @Override
  public String toString() {
    return "Guard " + getName() +" inputs: " + getInputs().values() ;
  }
}
