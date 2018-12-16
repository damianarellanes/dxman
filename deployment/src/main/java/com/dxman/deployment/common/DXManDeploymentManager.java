package com.dxman.deployment.common;

import com.dxman.design.connectors.common.DXManConnectorTemplate;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.common.*;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.utils.DXManIDGenerator;
import com.google.gson.*;
import org.eclipse.californium.core.CoapClient;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * @author Damian Arellanes
 */
public class DXManDeploymentManager {
  
  private final Gson GSON;
  
  public DXManDeploymentManager() {        
    GSON = new GsonBuilder().disableHtmlEscaping().create();
  }
  
  public void deployCompositeService(DXManCompositeServiceTemplate composite) {
        
    composite.getSubServices().forEach((subService) -> {
      
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
    
    deployAdapters(template);
        
    template.setClassTypeServ(); // For deserializing service template
    template.getConnector().setClassType(); // For deserializing connector template
    
    String thingTargetUri = DXManIDGenerator.getCoapUri(
      template.getConnector().getDeploymentInfo().getThingIp(), 
      template.getConnector().getDeploymentInfo().getThingPort(), 
      DXManIDGenerator.getDeployerUUID(
        template.getConnector().getDeploymentInfo().getThingAlias()
      )
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
    
    String connectorId = sendRequest(thingTargetUri, deploymentRequest);
    
    System.out.println("Connector deployed: " + connectorId);
  }
  
  private void deployAdapters(DXManServiceTemplate service) {
    
    for(DXManConnectorTemplate adapter: service.getAdapters()) {
      
      adapter.setClassType();
      
      String thingTargetUri = DXManIDGenerator.getCoapUri(
        adapter.getDeploymentInfo().getThingIp(), 
        adapter.getDeploymentInfo().getThingPort(), 
        DXManIDGenerator.getDeployerUUID(
          adapter.getDeploymentInfo().getThingAlias()
        )
      );
      
      DXManDeploymentRequest deploymentRequest = new DXManDeploymentRequest(
        adapter
      );
      
      String connectorId = sendRequest(thingTargetUri, deploymentRequest);
    
      System.out.println("Adapter deployed: " + connectorId);
    }
  }
  
  private String sendRequest(String uri, DXManDeploymentRequest deploymentRequest) {
    
    CoapClient cp = new CoapClient(uri);    
    // TODO the timeout should be infinite
    // Sets a large timeout for the execution
    cp.setTimeout(AsyncTaskExecutor.TIMEOUT_INDEFINITE);
    
    String connectorId = cp.post(GSON.toJson(deploymentRequest), 0).getResponseText();
    
    return connectorId;
  }
}
