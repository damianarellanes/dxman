package com.dxman.api;

import com.dxman.deployment.common.DXManDeploymentManager;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.common.DXManServiceTemplate;
import com.dxman.design.services.common.DXManServiceType;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * @author Damian Arellanes
 */
@Path("api")
@Singleton
public class API {
  
  private final DXManDeploymentManager DEPLOYMENT_MANAGER;

  public API() {
    this.DEPLOYMENT_MANAGER = new DXManDeploymentManager();
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("deploy")  
  public Response deploy(DXManServiceTemplate serviceTemplate) {    
    
    DEPLOYMENT_MANAGER.deployServiceTemplate(serviceTemplate);    
    return Response.status(200).entity("Atomic service deployed").build();
  }
}
