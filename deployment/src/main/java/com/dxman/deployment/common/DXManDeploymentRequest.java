package com.dxman.deployment.common;

import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.common.DXManServiceType;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;


/**
 * @author Damian Arellanes
 */
public class DXManDeploymentRequest {
  
  private final DXManServiceType serviceType;
  private DXManAtomicServiceTemplate atomicService;
  private DXManCompositeServiceTemplate compositeService;
  
  public DXManDeploymentRequest(DXManServiceType serviceType, 
    DXManAtomicServiceTemplate atomicService) {
    this.serviceType = serviceType;
    this.atomicService = atomicService;
  }
  
  public DXManDeploymentRequest(DXManServiceType serviceType, 
    DXManCompositeServiceTemplate compositeService) {
    this.serviceType = serviceType;
    this.compositeService = compositeService;
  }

  public DXManServiceType getServiceType() { return serviceType; }
  public DXManAtomicServiceTemplate getAtomicService() { return atomicService; }
  public DXManCompositeServiceTemplate getCompositeService() {
    return compositeService;
  }    
}
