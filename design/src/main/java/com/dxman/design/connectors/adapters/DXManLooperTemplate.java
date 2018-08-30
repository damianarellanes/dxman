package com.dxman.design.connectors.adapters;

import com.dxman.design.connectors.common.*;
import com.dxman.design.distribution.DXManDeploymentInfo;

/**
 * @author Damian Arellanes
 */
public class DXManLooperTemplate extends DXManConnectorTemplate {
    
  public DXManLooperTemplate(String name, DXManDeploymentInfo deploymentInfo) {
    super(name, DXManConnectorType.LOOPER, deploymentInfo);
  }

  @Override
  public String toString() {
    return "Looper (" + getId() + ") " + getName() +" inputs: " + getInputs().values() ;
  }    
}
