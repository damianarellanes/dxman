package test;

import com.dxman.deployment.cli.DXManWorkflowTreeDesigner;
import com.dxman.deployment.cli.DXManWorkflowTreeEditor;
import com.dxman.deployment.common.DXManDeploymentManager;
import com.dxman.deployment.data.DXManDataAlgorithm;
import com.dxman.design.connectors.composition.DXManParallelTemplate;
import com.dxman.design.connectors.composition.DXManParallelType;
import com.dxman.design.connectors.composition.DXManSelectorTemplate;
import com.dxman.execution.DXManWorkflowTree;
import com.dxman.design.connectors.composition.DXManSequencerTemplate;
import com.dxman.design.data.DXManOperation;
import com.dxman.design.data.DXManParameter;
import com.dxman.design.data.DXManParameterType;
import com.dxman.design.distribution.DXManBindingContent;
import com.dxman.design.distribution.DXManBindingInfo;
import com.dxman.design.distribution.DXManDeploymentInfo;
import com.dxman.design.distribution.DXManEndpointType;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.common.DXManServiceInfo;
import com.dxman.design.services.common.DXManServiceTemplate;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.DXManWfInvocation;
import com.dxman.execution.DXManWfNode;
import com.dxman.execution.DXManWfNodeCustom;
import com.dxman.execution.DXManWfNodeMapper;
import com.dxman.execution.DXManWfParallel;
import com.dxman.execution.DXManWfParallelCustom;
import com.dxman.execution.DXManWfResult;
import com.dxman.execution.DXManWfSequencer;
import com.dxman.execution.DXManWfSequencerCustom;
import com.dxman.execution.selector.DXManWfSelector;
import com.dxman.execution.selector.DXManWfSelectorCustom;
import com.dxman.utils.DXManErrors;
import com.dxman.utils.RuntimeTypeAdapterFactory;
import com.google.gson.GsonBuilder;
import com.google.gson.*;
import java.net.URI;
import java.net.URISyntaxException;
import org.javalite.http.Http;
import org.javalite.http.Post;

/**
 * @author Damian Arellanes
 */
public class DesignerTest {
  
  public static DXManDeploymentManager deploymentManager = new DXManDeploymentManager();
  
  private static DXManAtomicServiceTemplate designLoyaltyPointsBank() throws URISyntaxException {
    
    DXManBindingInfo createRecordBinding = new DXManBindingInfo(
      new URI("http://localhost:8080/loyaltypointsbank-microservice/api/createRecord"), 
      DXManEndpointType.HTTP_POST, 
      DXManBindingContent.APPLICATION_JSON, 
      DXManBindingContent.PLAIN, 
      "{\"name\":\"##name##\", \"address\":\"##addr##\", \"email\":\"##email##\"}", 
      "##id##"
    );
    
    DXManOperation createRecord = new DXManOperation("createRecord", createRecordBinding);
    DXManParameter name = new DXManParameter("name", DXManParameterType.INPUT, "string"); createRecord.addParameter(name);
    DXManParameter addr = new DXManParameter("addr", DXManParameterType.INPUT, "string"); createRecord.addParameter(addr);
    DXManParameter email = new DXManParameter("email", DXManParameterType.INPUT, "string"); createRecord.addParameter(email);
    DXManParameter id = new DXManParameter("id", DXManParameterType.OUTPUT, "string"); createRecord.addParameter(id);    
    
    DXManServiceInfo templateInfo = new DXManServiceInfo("LoyaltyPointsBank", "MusicCorp", 0);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("Alienware", "192.168.0.5", 5683);
    DXManAtomicServiceTemplate loyaltyPointsBank = new DXManAtomicServiceTemplate(templateInfo, "IC1", deploymentInfo);
    loyaltyPointsBank.addOperation(createRecord);
    
    return loyaltyPointsBank;
  }
  
  private static DXManAtomicServiceTemplate designCourier(int num, String op) throws URISyntaxException {
    
    DXManBindingInfo sendWelcBinding = new DXManBindingInfo(
      new URI("http://localhost:8080/courier" + num +"-microservice/api/" + op), 
      DXManEndpointType.HTTP_POST, 
      DXManBindingContent.QUERY_STRING, 
      DXManBindingContent.PLAIN, 
      "?addr=##addr##&name=##name##", 
      "##res##"
    );
    
    DXManOperation sendWelc = new DXManOperation(op, sendWelcBinding);
    DXManParameter addr = new DXManParameter("addr", DXManParameterType.INPUT, "string"); sendWelc.addParameter(addr);
    DXManParameter name = new DXManParameter("name", DXManParameterType.INPUT, "string"); sendWelc.addParameter(name);
    DXManParameter res = new DXManParameter("res", DXManParameterType.OUTPUT, "string"); sendWelc.addParameter(res);
    
    DXManServiceInfo templateInfo = new DXManServiceInfo("Courier"+num, "MusicCorp", 0);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("Alienware", "192.168.0.5", 5683);
    DXManAtomicServiceTemplate courier = new DXManAtomicServiceTemplate(templateInfo, "IC"+(num+1), deploymentInfo);
    courier.addOperation(sendWelc);
    
    return courier;
  }
  
  private static DXManAtomicServiceTemplate designEmail() throws URISyntaxException {
    
    DXManBindingInfo sendWelcEmailBinding = new DXManBindingInfo(
      new URI("http://localhost:8080/email-microservice/api/sendWelcEmail"), 
      DXManEndpointType.HTTP_POST, 
      DXManBindingContent.PLAIN, 
      DXManBindingContent.NO_CONTENT, 
      "##email##", 
      ""
    );
    
    DXManOperation sendWelcEmail = new DXManOperation("sendWelcEmail", sendWelcEmailBinding);
    DXManParameter email = new DXManParameter("email", DXManParameterType.INPUT, "string"); sendWelcEmail.addParameter(email);
    //DXManParameter res = new DXManParameter("res", DXManParameterType.OUTPUT, "string"); sendWelcEmail.addParameter(res);
    
    DXManServiceInfo templateInfo = new DXManServiceInfo("Email Service", "MusicCorp", 0);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("Alienware", "192.168.0.5", 5683);
    DXManAtomicServiceTemplate emailService = new DXManAtomicServiceTemplate(templateInfo, "IC4", deploymentInfo);
    emailService.addOperation(sendWelcEmail);
    
    return emailService;
  }
  
  private static DXManCompositeServiceTemplate designPost() throws URISyntaxException {
    
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("Alienware", "192.168.0.5", 5683);
    
    //DXManSelectorTemplate selector = new DXManSelectorTemplate("SEL1");
    DXManParallelTemplate parallel = new DXManParallelTemplate("PAR1", DXManParallelType.SYNC, deploymentInfo);
    DXManParameter addr = new DXManParameter("addr", DXManParameterType.INPUT, "string"); parallel.addInput(addr);
            
    DXManAtomicServiceTemplate courier1 = designCourier(1, "sendWelcStd");
    DXManAtomicServiceTemplate courier2 = designCourier(2, "sendWelcFast");    
    
    DXManServiceInfo templateInfo = new DXManServiceInfo("PostService", "Example", 0);    
    DXManCompositeServiceTemplate composite = new DXManCompositeServiceTemplate(templateInfo, parallel);
    
    composite.composeServices(courier1, courier2);
    
    return composite;
  }
  
  private static DXManCompositeServiceTemplate designSender() throws URISyntaxException {
    
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("Alienware", "192.168.0.5", 5683);
    
    DXManSequencerTemplate sequencer = new DXManSequencerTemplate("SEQ2", deploymentInfo);
    DXManCompositeServiceTemplate post = designPost();
    DXManAtomicServiceTemplate email = designEmail();
    
    DXManServiceInfo templateInfo = new DXManServiceInfo("SenderService", "Example", 0);    
    DXManCompositeServiceTemplate composite = new DXManCompositeServiceTemplate(templateInfo, sequencer);
    
    composite.composeServices(post, email);
    
    return composite;
  }
  
  private static DXManCompositeServiceTemplate designCustomer() throws URISyntaxException {
    
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("Alienware", "192.168.0.5", 5683);
    
    DXManSequencerTemplate sequencer = new DXManSequencerTemplate("SEQ3", deploymentInfo);
    DXManCompositeServiceTemplate sender = designSender();
    DXManAtomicServiceTemplate lpb = designLoyaltyPointsBank();
    
    DXManServiceInfo templateInfo = new DXManServiceInfo("CustomerService", "Example", 0);    
    DXManCompositeServiceTemplate composite = new DXManCompositeServiceTemplate(templateInfo, sequencer);
    
    composite.composeServices(sender, lpb);
    
    return composite;
  }
  
  public static void simulate(DXManWfNode wfNode) {
   
    System.out.println("------------------------");
    System.out.println("Executing: " + wfNode.getUri());
    System.out.println("Workflow ID: " + wfNode.getWorkflowId());
    System.out.println("Node: " + wfNode.getId());    
    
    if(wfNode.getClass().equals(DXManWfInvocation.class)) {
      
      System.out.println("Operation: " + wfNode.getId());
      return;
    } else if(wfNode.getClass().equals(DXManWfSequencer.class)) {
      for(DXManWfNode subNode: ((DXManWfSequencer)wfNode).getSequence()) {      
        simulate(subNode);
      }    
    } else if(wfNode.getClass().equals(DXManWfSelector.class)) {
      for(DXManWfNodeMapper subNode: wfNode.getSubnodeMappers()) {      
        System.out.println(((DXManWfSelectorCustom)subNode.getCustom()).getCondition());
      }
    }
    
    
  }
  
  public static void main(String[] args) throws URISyntaxException {
        
    DXManAtomicServiceTemplate loyaltyPointsBank = designLoyaltyPointsBank();
    DXManCompositeServiceTemplate post = designPost();    
    DXManCompositeServiceTemplate sender = designSender();
    DXManCompositeServiceTemplate customer = designCustomer();
    
//    Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
//    
//    Post response = Http.post("http://localhost:8080/dxman-platform/api/deploy-atomic", GSON.toJson(loyaltyPointsBank));
//    response.header("Content-type", "application/json");
//    
//    System.out.println(response.text());    
            
        
    DXManWorkflowTreeDesigner wfTreeManager = new DXManWorkflowTreeDesigner("http://localhost:3000");
    String workflowTreeFile = "/home/darellanes/DX-MAN-Platform/examples/music-corp/wf4";
    
    // GENERATE WORKFLOW FILES    
    //wfTreeManager.buildWorkflowTree(workflowTreeFile, customer);
    
    // READS WORKFLOW FROM FILE
    DXManWorkflowTree wfTree = wfTreeManager.readWorkflowTreeDescription(workflowTreeFile);    
    WfTreeTest wtEditor = new WfTreeTest(wfTree, "Workflow-Test-4");
    //WfTreeTest wtEditor = new WfTreeTest(wfTree, "ANOTHERWF");
    //WfTreeTest wtEditor = new WfTreeTest(wfTree, "INEXISTENT");
    
    // DEPLOY WORKFLOW FROM FILE
    //deploymentManager.deployCompositeService(wfTree.getCompositeService());
    wfTreeManager.deployWorkflow(wtEditor, false); // true when data channels are modified, false for using same data channels
    
    // EXECUTES WORKFLOW FROM FILE
    String topService = wfTree.getCompositeService().getId();
    //String topService = "2055cafa-77a4-47af-b784-ffc61802ea38";
    DXManWfResult wfResult = wfTreeManager.executeWorkflow(wtEditor, wfTree.getWt().get(topService));
    wfResult.forEach((outputId, outputVal) -> {
        System.out.println(outputId + " --> " + outputVal);
    });
    
    // TODO force to overwrite parameters in the blockchain, even if they already exist
    
    //simulate(wfTree.getWt().get(topService));
  }
}
