package com.dxman.deployment.common;

import com.dxman.design.connectors.common.DXManConnectorTemplate;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.common.DXManServiceType;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;


/**
 * @author Damian Arellanes
 */
public class DXManDeploymentRequest {
  
  private DXManDeploymentType deploymentType;
  
  private DXManServiceType serviceType;
  private DXManAtomicServiceTemplate atomicService;
  private DXManCompositeServiceTemplate compositeService;
  
  private DXManConnectorTemplate adapter;
  
  public DXManDeploymentRequest() {}
  
  public DXManDeploymentRequest( 
    DXManServiceType serviceType, DXManAtomicServiceTemplate atomicService) {
    this.deploymentType = DXManDeploymentType.SERVICE;
    this.serviceType = serviceType;
    this.atomicService = atomicService;
  }
  
  public DXManDeploymentRequest(DXManServiceType serviceType, 
    DXManCompositeServiceTemplate compositeService) {
    this.deploymentType = DXManDeploymentType.SERVICE;
    this.serviceType = serviceType;
    this.compositeService = compositeService;
  }
  
  public DXManDeploymentRequest(DXManConnectorTemplate adapter) {
    this.deploymentType = DXManDeploymentType.ADAPTER;
    this.adapter = adapter;
  }
  
  public DXManDeploymentType getDeploymentType() { return deploymentType; }
  public void setDeploymentType(DXManDeploymentType deploymentType) {
    this.deploymentType = deploymentType;
  }

  public DXManServiceType getServiceType() { return serviceType; }
  public void setServiceType(DXManServiceType serviceType) {
    this.serviceType = serviceType;
  }
  
  public DXManAtomicServiceTemplate getAtomicService() { return atomicService; }
  public void setAtomicService(DXManAtomicServiceTemplate atomicService) {
    this.atomicService = atomicService;
  }
  
  public DXManCompositeServiceTemplate getCompositeService() {
    return compositeService;
  }
  public void setCompositeService(DXManCompositeServiceTemplate compositeService) {
    this.compositeService = compositeService;
  }
  
  public DXManConnectorTemplate getAdapter() { return adapter; }
  public void setAdapter(DXManConnectorTemplate adapter) {
    this.adapter = adapter;
  }
}
