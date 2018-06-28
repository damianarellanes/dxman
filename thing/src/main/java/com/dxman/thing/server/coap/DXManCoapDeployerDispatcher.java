package com.dxman.thing.server.coap;

import com.dxman.deployment.common.DXManDeploymentRequest;
import com.dxman.design.services.common.DXManServiceType;
import com.dxman.thing.deployment.common.DXManDeployer;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import com.dxman.thing.server.base.DXManDeployerDispatcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Damian Arellanes
 */
public class DXManCoapDeployerDispatcher extends CoapResource 
  implements DXManDeployerDispatcher {
  
  private final DXManDeployer deployer;
  
  private final Gson gson;

  public DXManCoapDeployerDispatcher(String deployerResourceName, 
    DXManDeployer deployer) {
    
    super(deployerResourceName);
    this.deployer = deployer;
    
    gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  }

  @Override
  public void handlePOST(CoapExchange exchange) {
    
    exchange.accept();
    
    System.out.println("Deploying connector...");
    
    String connectorId = deployService(exchange.getRequestText());
    
    exchange.respond(CoAP.ResponseCode.CREATED, connectorId);
  }
  
  private String deployService(String jsonService) {
    
    DXManDeploymentRequest deploymentRequest = 
      gson.fromJson(jsonService, DXManDeploymentRequest.class);
    
    String connectorId = "DEPLOYMENT_ERROR";
    if(deploymentRequest.getServiceType().equals(DXManServiceType.ATOMIC)) {
      
      connectorId = deployer.deployAtomicService(
        deploymentRequest.getAtomicService()
      );
    } else if(deploymentRequest.getServiceType().equals(DXManServiceType.COMPOSITE)) {
      // TODO deploy composite service
    }
    
    return connectorId;
  }
}
