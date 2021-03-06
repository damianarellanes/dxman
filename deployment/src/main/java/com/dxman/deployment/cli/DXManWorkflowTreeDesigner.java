package com.dxman.deployment.cli;

//import com.dxman.dataspace.blockchain.DXManDataParameter;
import com.dxman.execution.wttree.*;
import com.dxman.execution.sequencer.DXManWfSequencer;
import com.dxman.execution.parallel.DXManWfParallel;
import com.dxman.execution.invocation.DXManWfInvocation;
import com.dxman.execution.common.*;
import com.dxman.dataspace.base.*;
import com.dxman.deployment.common.DXManDeploymentUtils;
import com.dxman.deployment.data.DXManDataAlgorithm;
import com.dxman.deployment.data.DXManDataProcessorInstance;
import com.dxman.design.connectors.common.DXManConnectorTemplate;
import com.dxman.design.data.*;
import com.dxman.design.services.common.*;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.guard.DXManWfGuard;
import com.dxman.execution.looper.DXManWfLooper;
import com.dxman.execution.selector.DXManWfSelector;
import com.dxman.utils.*;
import com.google.gson.*;
import java.io.*;
import java.util.*;
import org.eclipse.californium.core.CoapClient;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * @author Damian Arellanes
 */
public class DXManWorkflowTreeDesigner {
  
  private final DXManDataSpace dataspace;
  private final Gson SERIALIZATION_GSON;
  private final Gson DESERIALIZATION_GSON;
  
  private StringBuilder COMPOSITE_CONTENT;
  private final String COMPOSITE_EXTENSION = ".comp";
  private final String WT_EXTENSION = ".wt";
  
  public DXManWorkflowTreeDesigner(String dataspaceEndpoint) {     
    this.dataspace = DXManDataSpaceFactory.createBlockchainManager(dataspaceEndpoint);
    SERIALIZATION_GSON = DXManDeploymentUtils.buildSerializationGson();
    DESERIALIZATION_GSON = DXManDeploymentUtils.buildDeserializationGson();
  }
  
  public DXManWfResult executeWorkflow(DXManWorkflowTreeEditor wtEditor, 
    DXManWfNode node) {
        
    // Gets the workflow id
    String wfId = wtEditor.getWorkflowTree().getId();
    
    // Writes input parameters
    System.out.println("Updating inputs in the blockchain...");
    List<DXManDataParameter> wfInputs = new ArrayList<>();
    
    wtEditor.getInputs().forEach((paramId, paramValue)->{      
      wfInputs.add(dataspace.createDataParameter(paramId, wfId, paramValue, "user"));
    });
    dataspace.writeParameters(wfInputs);
    
    // Executes the workflow
    System.out.println("Executing the workflow " 
      + wtEditor.getWorkflowTree().getId() +"...");            
    CoapClient cp = new CoapClient(node.getUri());
    
    // TODO the timeout should be infinite
    // Sets a large timeout for the execution
    cp.setTimeout(AsyncTaskExecutor.TIMEOUT_INDEFINITE);
    
    cp.post(SERIALIZATION_GSON.toJson(node), 0);
    
    // Reads output parameters
    System.out.println("Reading outputs...");
    DXManWfResult outputValues = new DXManWfResult();
    for(String outputId: wtEditor.getOutputs()) {

      // Only reads outputs that have been update during the workflow execution
      String value = dataspace.readParameter(
        outputId, wfId, wtEditor.getWorkflowTree().getCreationTimestamp()
      );
      if(!value.equals(DXManErrors.PARAMETER_VALUE_NOT_FOUND.name()))
        outputValues.put(outputId, value);
    }
    
    return outputValues;
  }
  
  public void customizeConcreteWfTree(DXManWorkflowTreeEditor wtEditor, 
    boolean deployData) {
    
    DXManDataAlgorithm alg = new DXManDataAlgorithm();
    
    // Sets the workflow timestamp from the data space before deploying it
    String wfTimestamp = dataspace.getDataspaceTimestamp();
    wtEditor.getWorkflowTree().setCreationTimestamp(wfTimestamp);
     
    wtEditor.design();
    
    wtEditor.getWorkflowTree().getWt().get(
      wtEditor.getWorkflowTree().getCompositeService().getId()
    ).deploy(alg, wtEditor.getWorkflowTree());
    
    if(deployData) {  
      deployDataProcessors(wtEditor.getWorkflowTree().getDataProcessors());
      deployWorkflowDataChannels(alg, wtEditor.getWorkflowTree().getId());      
    }      
  }
  
  private void deployDataProcessors(List<DXManDataProcessorInstance> processors) {    
    processors.forEach((dataProcessor) -> { dataProcessor.connect(); });
  }
  
  private void deployWorkflowDataChannels(DXManDataAlgorithm alg, String wfId) {
    
    System.out.println("Deploying data channels...");
    
    DXManMap<String, DXManDataParameter> alreadyDeployed = new DXManMap<>();        
    List<DXManDataParameter> parametersToDeploy = new ArrayList<>();
    
    alg.getReaders().forEach((readerId, writers) -> {
      
      DXManDataParameter reader = dataspace.createDataParameter(readerId, wfId, "null", "none");
      
      for(String writerId: writers) {
            
        DXManDataParameter writer = alreadyDeployed.get(writerId);

        if(writer == null) {
          writer = dataspace.createDataParameter(writerId, wfId, "null", "none");
          parametersToDeploy.add(writer);
          alreadyDeployed.put(writerId, writer);
        }
        reader.addWriter(writer);
      }
      
      parametersToDeploy.add(reader);
      alreadyDeployed.put(readerId, reader);
    });
    
    dataspace.createParameters(parametersToDeploy, wfId);
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
    composite.setId();
    
    DXManWfNode parentWfNode = createWfNodeInstance(
      composite, composite.getId(), ""
    );
    
    // Analyses adapters connected to the connector of the composite service
    DXManWfNode topNode = parentWfNode;
    if(!composite.getAdapters().isEmpty()) {
      topNode = analyzeLinks(composite, null, parentWfNode, wt);
    }
    
    for(DXManServiceTemplate subService: composite.getSubServices()) {
      
      // Sets the id for the subservice which is used as the resource for the server
      subService.setId();
      
      if(subService.getType().equals(DXManServiceType.COMPOSITE)) {
        
        DXManWfNode subWfNode = generateWorkflowTree(
          (DXManCompositeServiceTemplate) subService, wt
        );
      
        connectWfNodes(parentWfNode, subWfNode);
      } else {
                
        if(subService.getAdapters().isEmpty()) {
          
          subService.getOperations().forEach((opId, op)->{        
            DXManWfNode opNode = createWfNodeInstance(subService, opId, opId);        
            analyzeLinks(subService, parentWfNode, opNode, wt);
            updateWorkflowTree(wt, opNode);
          });
        } else {
        
          DXManWfNode bottomAdapter = analyzeLinks(subService, parentWfNode, null, wt);
          subService.getOperations().forEach((opId, op)->{        
            DXManWfNode opNode = createWfNodeInstance(subService, opId, opId);
            connectWfNodes(bottomAdapter, opNode);
            updateWorkflowTree(wt, opNode);
            updateWorkflowTree(wt, bottomAdapter);
          });
        }
        
        COMPOSITE_CONTENT.append(subService);
      }
      
      generateAlgebraicDataChannels(composite, subService, parentWfNode, wt);
    }
    
    updateWorkflowTree(wt, topNode);
    updateWorkflowTree(wt, parentWfNode);
    
    COMPOSITE_CONTENT.append(composite);
    
    return topNode;
  }
  
  private DXManWfNode analyzeLinks(DXManServiceTemplate serviceTemplate, 
    DXManWfNode parent, DXManWfNode child, DXManWorkflowTree wt) {
    
    if(serviceTemplate.getAdapters().isEmpty()) {
      connectWfNodes(parent, child);
      return null;
    }
    
    DXManWfNode topAdapter = createWfAdapterInstance(
      serviceTemplate.getAdapters().get(serviceTemplate.getAdapters().size()-1));
    DXManWfNode bottomAdapter = topAdapter;
        
    if(parent != null) {
      connectWfNodes(parent, topAdapter);
    }
    
    for(int i = serviceTemplate.getAdapters().size()-2; i >= 0; i--) {
      
      DXManWfNode childAdapter = createWfAdapterInstance(
        serviceTemplate.getAdapters().get(i));            
      
      connectWfNodes(bottomAdapter, childAdapter);
      
      updateWorkflowTree(wt, bottomAdapter); 
      
      bottomAdapter = childAdapter;
    }
    
    if(child != null) {
      connectWfNodes(bottomAdapter, child);
      updateWorkflowTree(wt, bottomAdapter);
    } 
             
    updateWorkflowTree(wt, topAdapter);
    
    if(child != null)    
      return topAdapter;
    else
      return bottomAdapter;
  }
  
  private void connectWfNodes(DXManWfNode parent, DXManWfNode child) {
    
    //System.out.println("Connecting: " + parent.getId() + "--->" + child.getId());
    parent.addSubWfNode(child, null);    
  }
  
  private DXManWfNode createWfAdapterInstance(DXManConnectorTemplate connector) {
    
    String uri = DXManIDGenerator.getCoapUri(
      connector.getDeploymentInfo().getThingIp(), 
      connector.getDeploymentInfo().getThingPort(), 
      connector.getId()); // TODO The uri should be to the operation not the service/connector
    
    switch(connector.getType()) {
      case GUARD:
        return new DXManWfGuard(connector.getId(), uri);
      case LOOPER:
        return new DXManWfLooper(connector.getId(), uri);
    }
    return null;
  }
  
  private DXManWfNode createWfNodeInstance(DXManServiceTemplate service, 
    String wfNodeId, String operationName) {
    
    String uri = DXManIDGenerator.getCoapUri(
      service.getConnector().getDeploymentInfo().getThingIp(), 
      service.getConnector().getDeploymentInfo().getThingPort(), 
      service.getConnector().getId()); // TODO The uri should be to the operation not the service/connector
    
    if(service.getType().equals(DXManServiceType.ATOMIC)) {
      return new DXManWfInvocation(wfNodeId, uri, operationName);
    } else {
      
      switch(service.getConnector().getType()) {
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
    DXManWfNode wfNode, DXManWorkflowTree wt
  ) {
    
    if(wt.getDataChannels().get(wfNode.getId()) == null)
      wt.getDataChannels().put(wfNode.getId(), new ArrayList<>());
    
    // Adds all the operations of subservices to composite service
    for(DXManOperation op: subService.getOperations().values()) {
      
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
        wt.getDataChannels().get(wfNode.getId()).add(dc);
      });
    }
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
  
  public DXManDataSpace getDataspace() { return dataspace; }
}
