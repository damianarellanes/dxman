package com.dxman.thing.server.coap;

import com.dxman.deployment.common.DXManDeploymentRequest;
import com.dxman.design.connectors.composition.*;
import com.dxman.design.services.common.DXManServiceType;
import com.dxman.thing.deployment.common.DXManDeployer;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import com.dxman.thing.server.base.DXManDeployerDispatcher;
import com.dxman.utils.DXManErrors;
import com.dxman.utils.RuntimeTypeAdapterFactory;
import com.google.gson.*;

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
    
    // TODO Put all adapters in a single class
    RuntimeTypeAdapterFactory<DXManCompositionConnectorTemplate> adapter2 
      = RuntimeTypeAdapterFactory   
      .of(DXManCompositionConnectorTemplate.class, "classType")
      .registerSubtype(DXManParallelTemplate.class, DXManParallelTemplate.class.getName())
      .registerSubtype(DXManSequencerTemplate.class, DXManSequencerTemplate.class.getName())
      .registerSubtype(DXManSelectorTemplate.class, DXManSelectorTemplate.class.getName());
    
    gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
      .registerTypeAdapterFactory(adapter2)
      .create();
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
    
    String connectorId = DXManErrors.DEPLOYMENT_ERROR.toString();
    if(deploymentRequest.getServiceType().equals(DXManServiceType.ATOMIC)) {
      
      connectorId = deployer.deployAtomicService(
        deploymentRequest.getAtomicService()
      );
    } else if(deploymentRequest.getServiceType().equals(DXManServiceType.COMPOSITE)) {
      
      connectorId = deployer.deployCompositeService(
        deploymentRequest.getCompositeService()
      );
    }
    
    return connectorId;
  }
}
