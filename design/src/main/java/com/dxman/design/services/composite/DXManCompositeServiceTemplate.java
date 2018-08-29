package com.dxman.design.services.composite;

import com.dxman.design.connectors.common.*;
import com.dxman.design.services.common.*;
import java.util.*;

/**
 * @author Damian Arellanes
 */
public class DXManCompositeServiceTemplate extends DXManServiceTemplate {
  
  private List<DXManServiceTemplate> subServices;

  public DXManCompositeServiceTemplate(DXManServiceInfo info, 
    DXManConnectorTemplate compositionConnector) { // TODO this should an abstract class to allow only composition connectors
    
    super(info, DXManServiceType.COMPOSITE, compositionConnector);
    subServices = new ArrayList<>();
  };
  
  public void composeServices(DXManServiceTemplate... services) {
    subServices.addAll(Arrays.asList(services));
  }
  
  public List<DXManServiceTemplate> getSubServices() { return subServices; }
  public void setSubServices(List<DXManServiceTemplate> subServices) {
    this.subServices = subServices;
  }
  
  @Override
  public String toString() {
        
    StringBuilder sb = new StringBuilder();
            
    sb.append("***************************************************\n");
    sb.append("Service TEMPLATE: ").append(getInfo().getName())
      .append(" (COMPOSITE)-->").append(getId()).append("\n");
    sb.append("***************************************************\n");    
    if(getConnector().getType().equals(DXManConnectorType.SELECTOR)) {
      sb.append(getConnector()).append("\n");
      sb.append("***************************************************\n");       
    }
    sb.append("\n").append(getOperations().values());
    sb.append("***************************************************\n");
    sb.append("***************************************************\n\n\n");

    return sb.toString();
  }
}
