package com.dxman.design.services.atomic;

import com.dxman.design.distribution.DXManDeploymentInfo;
import com.dxman.design.services.common.*;

/**
 * @author darellanes
 */
public class DXManAtomicServiceTemplate extends DXManServiceTemplate {
    
  private DXManComputationUnit computationUnit;

  public DXManAtomicServiceTemplate() {
    
    super(new DXManServiceInfo(), DXManServiceType.ATOMIC, 
      new DXManDeploymentInfo());
  }

  public DXManAtomicServiceTemplate(DXManServiceInfo info, 
    DXManComputationUnit computationUnit, DXManDeploymentInfo deploymentInfo) {
    
    super(info, DXManServiceType.ATOMIC, deploymentInfo);
    this.computationUnit = computationUnit;
  };

  public DXManComputationUnit getComputationUnit() { return computationUnit; }
  public void setComputationUnit(DXManComputationUnit computationUnit) {
    this.computationUnit = computationUnit;
  }
}
