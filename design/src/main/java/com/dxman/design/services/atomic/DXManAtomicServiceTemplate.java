package com.dxman.design.services.atomic;

import com.dxman.design.connectors.atomic.DXManInvocationTemplate;
import com.dxman.design.distribution.DXManDeploymentInfo;
import com.dxman.design.services.common.*;

/**
 * @author darellanes
 */
public class DXManAtomicServiceTemplate extends DXManServiceTemplate {
    
  private DXManInvocationTemplate invocationConnector;
  private DXManComputationUnit computationUnit;

  public DXManAtomicServiceTemplate() {
    
    super(new DXManServiceInfo(), DXManServiceType.ATOMIC, 
      new DXManDeploymentInfo());
  }

  public DXManAtomicServiceTemplate(DXManServiceInfo info, String connectorName,
    DXManComputationUnit computationUnit, DXManDeploymentInfo deploymentInfo) {
    
    super(info, DXManServiceType.ATOMIC, deploymentInfo);
    this.invocationConnector = new DXManInvocationTemplate(connectorName);
    this.computationUnit = computationUnit;
  };
  
  public DXManInvocationTemplate getInvocationConnector() {
    return invocationConnector;
  }
  public void setInvocationConnector(DXManInvocationTemplate invocationConnector) {
    this.invocationConnector = invocationConnector;
  }

  public DXManComputationUnit getComputationUnit() { return computationUnit; }
  public void setComputationUnit(DXManComputationUnit computationUnit) {
    this.computationUnit = computationUnit;
  }
  
  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();
    sb.append("***************************************************\n");
    sb.append("Service TEMPLATE: ").append(getInfo().getName())
      .append(" (ATOMIC)-->").append(getId()).append("\n");
    sb.append(getOperations().values());
    sb.append("***************************************************\n");
    sb.append("***************************************************\n\n\n");

    return sb.toString();
  }
}
