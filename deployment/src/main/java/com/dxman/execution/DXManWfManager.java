package com.dxman.execution;

import com.dxman.dataspace.base.*;
import com.dxman.deployment.data.DXManDataAlgorithm;
import com.dxman.design.data.*;
import com.dxman.design.services.common.*;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.utils.*;
import com.google.gson.*;

/**
 * @author Damian Arellanes
 */
public class DXManWfManager {
  
  private final DXManDataSpace dataSpace;
  private final Gson GSON;
    
  public DXManWfManager() {
    
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
  
  public void buildWorkflowTree(DXManWorkflowTree wfTree, DXManDataAlgorithm alg) {
    
    //WfTreeTest wf = new WfTreeTest(customer);
    generateWT(wfTree.getCompositeService(), wfTree);        
    
    wfTree.design();
    
    wfTree.get(wfTree.getCompositeService().getInfo().getName()).deploy(alg);
  }
  
  private DXManWfNode generateWT(DXManCompositeServiceTemplate composite, 
      DXManWorkflowTree wt) {
    
    DXManWfNode parentWfNode = createWfNodeInstance(composite);
    
    for(DXManServiceTemplate subService: 
      composite.getCompositionConnector().getSubServices()) {
      
      // Adds the operations to the workflow tree      
      subService.getOperations().forEach((opName, op)->{
        
        DXManWfInvocation opNode = new DXManWfInvocation(
          opName,
          op.getBindingInfo().getEndpoint().toString()
        );            
        parentWfNode.composeWf(opNode, new DXManWfNodeCustom() {});
      });
      
      DXManWfNode subWfNode;
      if(subService.getType().equals(DXManServiceType.COMPOSITE)) {
        
        subWfNode = generateWT((DXManCompositeServiceTemplate) subService, wt);
        parentWfNode.composeWf(subWfNode, new DXManWfNodeCustom() {});
      }
      
      generateAlgebraicDataChannels(composite, subService, parentWfNode);
    }
    
    updateWorkflowTree(wt, parentWfNode);
    
    return parentWfNode;
  }
  
  private DXManWfNode createWfNodeInstance(DXManServiceTemplate service) {
    
    String id = service.getInfo().getName();
    String uri = DXManIDGenerator.getCoapUri(
      service.getDeploymentInfo().getThingIp(), 
      service.getDeploymentInfo().getThingPort(), 
      service.getInfo().getName());
    
    if(service.getType().equals(DXManServiceType.ATOMIC)) {
      return new DXManWfInvocation(id, uri);      
    } else {
      
      switch(((DXManCompositeServiceTemplate) service).getCompositionConnector().getType()) {
      case SEQUENCER:
        return new DXManWfSequencer(id, uri);
    }
    }
    return null;    
  }
  
  private void updateWorkflowTree(DXManWorkflowTree workflowTree, DXManWfNode wfNode) {
    workflowTree.put(wfNode.getId(), wfNode);
  }
  
  private void generateAlgebraicDataChannels(
    DXManCompositeServiceTemplate composite, DXManServiceTemplate subService,
    DXManWfNode wfNode
  ) {
    
    // Adds all the operations of subservices to composite service
    subService.getOperations().forEach((opName, op)->{

      DXManOperation compositeOp = op.clone();
      composite.addOperation(compositeOp);

      op.getParameters().forEach((parName, par)->{

        // Generates a new id for the existent parameter (this should be done per workflow)
        par.setId(DXManIDGenerator.generateParameterID(parName));
                
        DXManDataChannelPoint origin;
        DXManDataChannelPoint destination;
        if(par.getParameterType().equals(DXManParameterType.INPUT)) {

          origin = new DXManDataChannelPoint(
            compositeOp.getParameters().get(parName).getId(), parName, 
            opName, composite.getInfo().getName()
          );            
          destination = new DXManDataChannelPoint(
            par.getId(), parName, opName, subService.getInfo().getName()
          );
        } else {

          origin = new DXManDataChannelPoint(
            par.getId(), parName, opName, subService.getInfo().getName()
          );
          destination = new DXManDataChannelPoint(
            compositeOp.getParameters().get(parName).getId(), parName,
            opName, composite.getInfo().getName()
          );
        }

        DXManDataChannel dc = new DXManDataChannel(origin, destination);
        wfNode.getDataChannels().add(dc);
      });
    });
  }
  
  public DXManWfResult executeWorkflow(DXManWfNode topWfNode) {
    
    return null;
    /*topWfTree.build();
    
    // Writes input parameters
    topWfTree.getInputs().forEach((paramId, value)->{
      dataSpace.writeParameter(paramId, value);
    });
        
    CoapClient cp = new CoapClient(topWfTree.getWfNode().getUri());
    cp.post(GSON.toJson(topWfTree.getWfNode()), 0);
    
    DXManWfResult outputValues = new DXManWfResult();
    for(String outputId: topWfTree.getOutputs()) {      
      outputValues.put(outputId, dataSpace.readParameter(outputId));
    }
    
    return outputValues;*/
  } 
}
