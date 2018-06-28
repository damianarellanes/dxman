package com.dxman.execution;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.dataspace.base.DXManDataSpaceFactory;
import com.dxman.utils.RuntimeTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.californium.core.CoapClient;

/**
 * @author Damian Arellanes
 */
public class DXManWorkflowManager {
  
  private final DXManDataSpace dataSpace;
  private final Gson GSON;
    
  public DXManWorkflowManager() {
    
    dataSpace = DXManDataSpaceFactory.createBlockchainManager(
      "http://localhost:3000"
    );
    
    RuntimeTypeAdapterFactory<DXManWfNode> adapter = RuntimeTypeAdapterFactory
      .of(DXManWfNode.class, "wfnode")
      .registerSubtype(DXManWfInvocation.class, "wfinvocation");
    
    GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
      .registerTypeAdapterFactory(adapter).create();
  }
  
  public void executeWorkflow(DXManWfSpec wfSpec, DXManWorkflowData wfData) {
    
    wfData.forEach((paramId, value)->{
      dataSpace.writeParameter(paramId, value);
    });
        
    CoapClient cp = new CoapClient(wfSpec.getFlow().getUri());
    cp.post(GSON.toJson(wfSpec.getFlow()), 0);
  } 
}
