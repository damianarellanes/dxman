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
    
    RuntimeTypeAdapterFactory<DXManWfNode> adapter1 = RuntimeTypeAdapterFactory
      .of(DXManWfNode.class, "wfnode")
      .registerSubtype(DXManWfParallel.class, "wfparallel")
      .registerSubtype(DXManWfSequencer.class, "wfsequencer")
      //.registerSubtype(WfGuard.class, "wfguard")
      .registerSubtype(DXManWfInvocation.class, "wfinvocation");
    RuntimeTypeAdapterFactory<DXManWfNodeCustom> adapter2 = RuntimeTypeAdapterFactory
      .of(DXManWfNodeCustom.class, "wfnodecustom")
      .registerSubtype(DXManWfParallelCustom.class, "wfparallelcustom")
      .registerSubtype(DXManWfSequencerCustom.class, "wfsequencercustom");
    
    GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
      .registerTypeAdapterFactory(adapter1).registerTypeAdapterFactory(adapter2)
      .create();
  }
  
  public DXManWorkflowData executeWorkflow(DXManWfSpec wfSpec, 
    DXManWorkflowData wfInputs, DXManWorkflowOutputs wfOutputs) {
    
    // Writes input parameters
    wfInputs.forEach((paramId, value)->{
      dataSpace.writeParameter(paramId, value);
    });
        
    CoapClient cp = new CoapClient(wfSpec.getFlow().getUri());
    cp.post(GSON.toJson(wfSpec.getFlow()), 0);
    
    DXManWorkflowData outputValues = new DXManWorkflowData();
    for(String outputId: wfOutputs) {      
      outputValues.put(outputId, dataSpace.readParameter(outputId));
    }
    
    return outputValues;
  } 
}
