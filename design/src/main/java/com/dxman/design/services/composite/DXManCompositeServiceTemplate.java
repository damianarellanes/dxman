package com.dxman.design.services.composite;

import com.dxman.design.services.common.DXManServiceTemplate;
import com.dxman.design.connectors.composition.DXManCompositionConnectorTemplate;
import com.dxman.design.distribution.DXManDeploymentInfo;
import com.dxman.design.services.common.*;

/**
 * @author Damian Arellanes
 */
public class DXManCompositeServiceTemplate extends DXManServiceTemplate {
  
  private DXManCompositionConnectorTemplate compositionConnector;
  
  public DXManCompositeServiceTemplate() {
    
    super(new DXManServiceInfo(), DXManServiceType.COMPOSITE, 
      new DXManDeploymentInfo());
  }

  public DXManCompositeServiceTemplate(DXManServiceInfo info, 
    DXManCompositionConnectorTemplate compositionConnector, 
    DXManDeploymentInfo deploymentInfo) {
    
    super(info, DXManServiceType.COMPOSITE, deploymentInfo);
    this.compositionConnector = compositionConnector;
  };

  public DXManCompositionConnectorTemplate getCompositionConnector() {
    return compositionConnector;
  }
  public void setCompositionConnector(DXManCompositionConnectorTemplate 
    compositionConnector) {
    this.compositionConnector = compositionConnector;
  }
}
