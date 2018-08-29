package com.dxman.design.connectors.composition;

import com.dxman.design.connectors.common.*;
import com.dxman.design.distribution.DXManDeploymentInfo;

/**
 * @author Damian Arellanes
 */
public class DXManSequencerTemplate extends DXManConnectorTemplate {
    
  public DXManSequencerTemplate(String name, DXManDeploymentInfo deploymentInfo) {
    super(name, DXManConnectorType.SEQUENCER, deploymentInfo);
  }    
}
