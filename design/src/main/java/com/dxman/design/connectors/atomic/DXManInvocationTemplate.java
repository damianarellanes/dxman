package com.dxman.design.connectors.atomic;

import com.dxman.design.connectors.common.*;
import com.dxman.design.distribution.DXManDeploymentInfo;

/**
 * @author Damian Arellanes
 */
public class DXManInvocationTemplate extends DXManConnectorTemplate {
    
  public DXManInvocationTemplate(String name, DXManDeploymentInfo deploymentInfo) {
    super(name, DXManConnectorType.INVOCATION, deploymentInfo);
  }    
}
