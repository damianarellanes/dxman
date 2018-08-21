package com.dxman.execution;

import com.dxman.dataspace.base.*;
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
  
  private void updateWorkflowTree(DXManWorkflowTree workflowTree, DXManWfNode wfNode) {
    workflowTree.put(wfNode.getId(), wfNode);
  }
  
  public DXManWfNode generateWT(DXManCompositeServiceTemplate composite, 
      DXManWorkflowTree wt) {
    
    DXManWfNode parentWfNode = createWorkflowTreeInstance(composite);
    
    for(DXManServiceTemplate subService: 
      composite.getCompositionConnector().getSubServices()) {
      
      // Adds the operations to the workflow tree      
      subService.getOperations().forEach((opName, op)->{
        
        DXManWfInvocation opNode = new DXManWfInvocation(
          opName,
          op.getBindingInfo().getEndpoint().toString()
        );
                
        /*DXManWorkflowTreeNode opNode = new DXManWorkflowTreeInv( 
          opName,
          op.getBindingInfo().getEndpoint().toString()
        );*/
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
  
  public void generateAlgebraicDataChannels(
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
        
        //System.out.println(dc);
      });
    });
  }
  
  private DXManWfNode createWorkflowTreeInstance(DXManServiceTemplate service) {
    
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
