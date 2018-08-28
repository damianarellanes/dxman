package com.dxman.deployment.cli;

import com.dxman.dataspace.base.*;
import com.dxman.deployment.common.DXManDeploymentUtils;
import com.dxman.deployment.data.DXManDataAlgorithm;
import com.dxman.design.data.*;
import com.dxman.design.services.common.*;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.*;
import com.dxman.execution.selector.DXManWfSelector;
import com.dxman.utils.*;
import com.google.gson.*;
import java.io.*;
import java.util.*;
import org.eclipse.californium.core.CoapClient;

/**
 * @author Damian Arellanes
 */
public class DXManWorkflowTreeDesigner {
  
  private final DXManDataSpace dataSpace;
  private final Gson SERIALIZATION_GSON;
  private final Gson DESERIALIZATION_GSON;
  
  private StringBuilder COMPOSITE_CONTENT;
  private final String COMPOSITE_EXTENSION = ".comp";
  private final String WT_EXTENSION = ".wt";
  
  public DXManWorkflowTreeDesigner(String dataSpaceLocation) {    
    
    dataSpace = DXManDataSpaceFactory.createBlockchainManager(dataSpaceLocation);    
    SERIALIZATION_GSON = DXManDeploymentUtils.buildSerializationGson();
    DESERIALIZATION_GSON = DXManDeploymentUtils.buildDeserializationGson();
  }
  
  public DXManWfResult executeWorkflow(DXManWorkflowTreeEditor wtEditor, 
    DXManWfNode node) {
        
    String wfId = wtEditor.getWorkflowTree().getId();
    String wfTimestamp = wtEditor.getWorkflowTree().getCreationTimestamp();
    
    // Writes input parameters
    System.out.println("Updating inputs in the blockchain...");
    List<DXManDataParameter> dp = new ArrayList<>();
    
    wtEditor.getInputs().forEach((paramId, paramValue)->{
      dp.add(new DXManDataParameter(paramId, wfId, paramValue, 
        new ArrayList<>()));
    });
    dataSpace.writeParameters(dp, wfId);
    
    // Executes the workflow
    System.out.println("Executing the workflow " 
      + wtEditor.getWorkflowTree().getId() +"...");
    CoapClient cp = new CoapClient(node.getUri());
    cp.post(SERIALIZATION_GSON.toJson(node), 0);
    
    // Reads output parameters
    DXManWfResult outputValues = new DXManWfResult();
    for(String outputId: wtEditor.getOutputs()) {

      // Only reads outputs that have been update during the workflow execution
      String value = dataSpace.readParameter(outputId, wfId, wfTimestamp);
      if(!value.equals(DXManErrors.PARAMETER_VALUE_NOT_FOUND.name()))
        outputValues.put(outputId, value);
    }
    
    return outputValues;
  }
  
  public void deployWorkflow(DXManWorkflowTreeEditor wtEditor, 
    boolean deployDataChannels) {
    
    wtEditor.getWorkflowTree().updateCreationTimestamp();
    DXManDataAlgorithm alg = new DXManDataAlgorithm();
 
    wtEditor.design();
    
    wtEditor.getWorkflowTree().getWt().get(
      wtEditor.getWorkflowTree().getCompositeService().getId()
    ).deploy(alg, wtEditor.getWorkflowTree());
    
    if(deployDataChannels)
      deployWorkflowDataChannels(alg, wtEditor.getWorkflowTree().getId());
  }
  
  private void deployWorkflowDataChannels(DXManDataAlgorithm alg, String wfId) {
    
    System.out.println("Deploying data channels for workflow " + wfId + "...");
    
    // TODO Optimize this (perhaps sending the whole readers to every WfNode of the WfTree)
    // So every WfNode can access the data pipes from there
    DXManMap<String, String> alreadyDeployed = new DXManMap<>();
    List<DXManDataParameter> parametersToDeploy = new ArrayList<>();
    alg.getWriters().forEach((writerId, readers) ->{
            
      List<String> setReaders = new ArrayList<>();
      for(String reader: readers) {
      
        if(alreadyDeployed.get(reader) == null) {
      
          parametersToDeploy.add(new DXManDataParameter(
            reader, wfId, "null", new ArrayList<>())
          );
          alreadyDeployed.put(reader, reader);
        }          
        setReaders.add(reader);        
      }
      
      parametersToDeploy.add(new DXManDataParameter(
        writerId, wfId, "null", setReaders)
      );
      alreadyDeployed.put(writerId, writerId);      
    });
    
    dataSpace.registerParameters(parametersToDeploy, wfId);
  }
  
  public DXManWorkflowTree readWorkflowTreeDescription(String fileName) {
    
    return DESERIALIZATION_GSON.fromJson(
      readFile(fileName + WT_EXTENSION), 
      DXManWorkflowTree.class
    );
  }
  
  public void buildWorkflowTree(String fileName, 
    DXManCompositeServiceTemplate composite) {
    
    DXManWorkflowTree wfTree = new DXManWorkflowTree(composite);        
    COMPOSITE_CONTENT = new StringBuilder();

    // Generates the workflow tree
    generateWorkflowTree(composite, wfTree);
    
    // Creates the json represenattion of the workflow tree
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();    
    writeFile(fileName + WT_EXTENSION, gson.toJson(wfTree));
    writeFile(fileName + COMPOSITE_EXTENSION, COMPOSITE_CONTENT.toString());
    
    System.out.println("The workflow tree for " + composite.getInfo().getName()
      + " has been created in the file " + fileName + WT_EXTENSION);
    System.out.println("The composite service template description for " 
      + composite.getInfo().getName() + " has been created in the file " 
      + fileName + COMPOSITE_EXTENSION);
  }
    
  private DXManWfNode generateWorkflowTree(
    DXManCompositeServiceTemplate composite, DXManWorkflowTree wt) {
    
    // Sets the id for the current service which is used as the resource for the server
    composite.setId(DXManIDGenerator.generateServiceID());
    
    DXManWfNode parentWfNode = createWfNodeInstance(
      composite, composite.getId(), ""
    );
    
    for(DXManServiceTemplate subService: 
      composite.getCompositionConnector().getSubServices()) {
      
      // Sets the id for the subservice which is used as the resource for the server
      subService.setId(DXManIDGenerator.generateServiceID());
      
      // Adds the operations to the workflow tree      
      subService.getOperations().forEach((opId, op)->{
        
        DXManWfNode opNode = createWfNodeInstance(subService, opId, opId);
        parentWfNode.addSubWfNode(opNode, new DXManWfNodeCustom() {});
        
        updateWorkflowTree(wt, opNode);
      });
      
      if(subService.getType().equals(DXManServiceType.COMPOSITE)) {
        
        DXManWfNode subWfNode = generateWorkflowTree(
          (DXManCompositeServiceTemplate) subService, wt
        );
        parentWfNode.addSubWfNode(subWfNode, new DXManWfNodeCustom() {});
      } else {
        COMPOSITE_CONTENT.append(subService);
      }
      
      generateAlgebraicDataChannels(composite, subService, parentWfNode);
    }
    
    updateWorkflowTree(wt, parentWfNode);
    
    COMPOSITE_CONTENT.append(composite);
    
    return parentWfNode;
  }
  
  private DXManWfNode createWfNodeInstance(DXManServiceTemplate service, 
    String wfNodeId, String operationName) {
    
    String uri = DXManIDGenerator.getCoapUri(
      service.getDeploymentInfo().getThingIp(), 
      service.getDeploymentInfo().getThingPort(), 
      service.getId()); // TODO The uri should be to the operation not the service
    
    if(service.getType().equals(DXManServiceType.ATOMIC)) {
      return new DXManWfInvocation(wfNodeId, uri, operationName);
    } else {
      
      switch(((DXManCompositeServiceTemplate) service).getCompositionConnector().getType()) {
      case SEQUENCER:
        return new DXManWfSequencer(wfNodeId, uri);
      case SELECTOR:
        return new DXManWfSelector(wfNodeId, uri);
      case PARALLEL:
        return new DXManWfParallel(wfNodeId, uri);
    }
    }
    return null;    
  }
  
  private void updateWorkflowTree(DXManWorkflowTree workflowTree, DXManWfNode wfNode) {
    workflowTree.getWt().put(wfNode.getId(), wfNode);
  }
  
  private void generateAlgebraicDataChannels(
    DXManCompositeServiceTemplate composite, DXManServiceTemplate subService,
    DXManWfNode wfNode
  ) {
    
    // Adds all the operations of subservices to composite service
    subService.getOperations().forEach((opId, op)->{

      DXManOperation compositeOp = op.clone();
      composite.addOperation(compositeOp);

      op.getParameters().forEach((parName, par)->{
                
        DXManDataChannelPoint origin;
        DXManDataChannelPoint destination;
        if(par.getParameterType().equals(DXManParameterType.INPUT)) {

          origin = new DXManDataChannelPoint(
            compositeOp.getInputs().get(parName).getId()
          );            
          destination = new DXManDataChannelPoint(par.getId());
        } else {

          origin = new DXManDataChannelPoint(par.getId());
          destination = new DXManDataChannelPoint(
            compositeOp.getOutputs().get(parName).getId()
          );
        }

        DXManDataChannel dc = new DXManDataChannel(origin, destination);
        wfNode.getDataChannels().add(dc);
      });
    });
  }
  
  private String readFile(String fileName) {
    
    String json = "";
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(fileName));
      json = reader.readLine();
      reader.close();
    } catch (FileNotFoundException ex) { System.out.println(ex); } 
      catch (IOException ex) { System.out.println(ex); } 
    finally {
      try { reader.close(); } catch (IOException ex) { System.out.println(ex); }
    }
    
    return json;
  }
  
  private void writeFile(String fileName, String json) {
    
    BufferedWriter writer;
    try {
      
      writer = new BufferedWriter(new FileWriter(fileName, false));
      writer.append(json);

      writer.close();
    } catch (IOException ex) { System.out.println(ex.toString()); }
  }
}
