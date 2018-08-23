package com.dxman.deployment.common;

import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.common.*;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.utils.DXManIDGenerator;
import com.google.gson.*;
import org.eclipse.californium.core.CoapClient;

/**
 * @author Damian Arellanes
 */
public class DXManDeploymentManager {
  
  private final Gson GSON;
  
  public DXManDeploymentManager() {        
    GSON = new GsonBuilder().disableHtmlEscaping().create();
  }
  
  public void deployCompositeService(DXManCompositeServiceTemplate composite) {
        
    composite.getCompositionConnector().getSubServices().forEach((subService) -> {
      
      if(subService.getType().equals(DXManServiceType.COMPOSITE)) {
        deployCompositeService((DXManCompositeServiceTemplate) subService);
      } else {
        deployServiceTemplate(subService);
      }      
    });    
    deployServiceTemplate(composite);
  }
  
  public void deployServiceTemplate(DXManServiceTemplate template) {
    
    System.out.println("Deploying --> " + template.getInfo().getName());
    
    // TODO change from deployer to deployer-UUID (/serviceName-serviceId)
    String thingTargetUri = DXManIDGenerator.getCoapUri(
      template.getDeploymentInfo().getThingIp(), 
      template.getDeploymentInfo().getThingPort(), 
      "deployer"
    ); 
    
    DXManDeploymentRequest deploymentRequest;
    if(template.getType().equals(DXManServiceType.ATOMIC)) {
      
      deploymentRequest = new DXManDeploymentRequest(
        DXManServiceType.ATOMIC, (DXManAtomicServiceTemplate) template
      );
    } else {
      
      deploymentRequest = new DXManDeploymentRequest(
        DXManServiceType.COMPOSITE, (DXManCompositeServiceTemplate) template
      );
    }
    
    new CoapClient(thingTargetUri).post(GSON.toJson(deploymentRequest), 0);
  }
}
