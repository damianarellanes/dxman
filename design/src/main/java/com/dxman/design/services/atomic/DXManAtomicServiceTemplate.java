package com.dxman.design.services.atomic;

import com.dxman.design.connectors.atomic.DXManInvocationTemplate;
import com.dxman.design.connectors.common.DXManConnectorTemplate;
import com.dxman.design.distribution.DXManDeploymentInfo;
import com.dxman.design.services.common.*;

/**
 * @author darellanes
 */
public class DXManAtomicServiceTemplate extends DXManServiceTemplate {

  public DXManAtomicServiceTemplate(DXManServiceInfo info, String connectorName,
    DXManDeploymentInfo deploymentInfo) {
    
    super(info, DXManServiceType.ATOMIC, 
      new DXManInvocationTemplate(connectorName, deploymentInfo));
  };
  
  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();
    sb.append("***************************************************\n");
    sb.append("Service TEMPLATE: ").append(getInfo().getName())
      .append(" (ATOMIC)-->").append(getId()).append("\n");
    sb.append("***************************************************\n"); 
    for(DXManConnectorTemplate adapter: getAdapters()) {
      sb.append(adapter).append("\n");
      sb.append("***************************************************\n");       
    }
    sb.append(getOperations().values());
    sb.append("***************************************************\n");
    sb.append("***************************************************\n\n\n");

    return sb.toString();
  }
}
