package esp8266multilevel;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.dataspace.base.DXManDataSpaceFactory;
import com.dxman.deployment.common.DXManDeploymentManager;
import com.dxman.deployment.data.DXManDataDeployer;
import com.dxman.design.connectors.composition.DXManParallelTemplate;
import com.dxman.design.connectors.composition.DXManParallelType;
import com.dxman.design.connectors.composition.DXManSequencerTemplate;
import com.dxman.design.data.*;
import com.dxman.design.distribution.*;
import com.dxman.design.services.atomic.*;
import com.dxman.design.services.common.*;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import java.net.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author Damian Arellanes
 */
public class DesignerMultilevel {
  
  private final DXManDeploymentManager deploymentManager;
  
  public DesignerMultilevel() {
    deploymentManager = new DXManDeploymentManager();
  }
  
  private DXManAtomicServiceTemplate designAtomicLed(int ledNumber) throws URISyntaxException {
    
    // Designs operation "Led" with binding and parameters
    
    DXManBindingInfo onOperationBinding = new DXManBindingInfo(
      new URI("http://192.168.0.16/led"), 
      DXManEndpointType.HTTP_GET, 
      DXManBindingContent.QUERY_STRING, 
      DXManBindingContent.APPLICATION_JSON, 
      "?params=%7B%22status%22%3A%22on%22%7D", 
      "{\"return_value\": ##return_value##, \"id\": \"##id##\", \"name\": \"##name##\", \"hardware\": \"##hardware##\", \"connected\": ##connected##}"
    );
    DXManBindingInfo offOperationBinding = new DXManBindingInfo(
      new URI("http://192.168.0.16/led"), 
      DXManEndpointType.HTTP_GET, 
      DXManBindingContent.QUERY_STRING, 
      DXManBindingContent.APPLICATION_JSON, 
      "?params=%7B%22status%22%3A%22off%22%7D", 
      "{\"return_value\": ##return_value##, \"id\": \"##id##\", \"name\": \"##name##\", \"hardware\": \"##hardware##\", \"connected\": ##connected##}"
    );
    
    DXManOperation onOperation = new DXManOperation("on", onOperationBinding);
    DXManOperation offOperation = new DXManOperation("off", offOperationBinding);
    
    // Designs the atomic service template for "LedService"
    DXManComputationUnit cu = new DXManComputationUnit();
    DXManServiceInfo templateInfo = new DXManServiceInfo("LedService" + ledNumber, "Category ESP8266", 86.79);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("192.168.0.5", 5683);
    DXManAtomicServiceTemplate ledServiceTemplate = new DXManAtomicServiceTemplate(templateInfo, cu, deploymentInfo);
    ledServiceTemplate.addOperation(onOperation);
    ledServiceTemplate.addOperation(offOperation);
    
    return ledServiceTemplate;
  }
  
  private DXManAtomicServiceTemplate designAtomicCalculator(int calcNumber) throws URISyntaxException {
    
    // Designs operation "multiply" with binding and parameters
    
    DXManBindingInfo multiplyBinding = new DXManBindingInfo(
      new URI("tcp://localhost:9090"), 
      DXManEndpointType.RAW_SOCKET, 
      DXManBindingContent.APPLICATION_JSON, 
      DXManBindingContent.APPLICATION_JSON, 
      "{\"operation\":\"multiply\", \"parameters\":[##n1##,##n2##]}", 
      "{\"result\":##result##}"
    );

    DXManOperation multiply = new DXManOperation("multiply", multiplyBinding);
    DXManParameter n1 = new DXManParameter("n1", DXManParameterType.INPUT, "double"); multiply.addParameter(n1);
    DXManParameter n2 = new DXManParameter("n2", DXManParameterType.INPUT, "double"); multiply.addParameter(n2);                
    DXManParameter result = new DXManParameter("result", DXManParameterType.OUTPUT, "double"); multiply.addParameter(result);
    
    // Designs the atomic service template for "MyCalculator"
    DXManComputationUnit cu = new DXManComputationUnit();
    DXManServiceInfo templateInfo = new DXManServiceInfo("Calculator" + calcNumber, "Category Maths", 100);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("192.168.0.5", 5683);
    DXManAtomicServiceTemplate myCalculator = new DXManAtomicServiceTemplate(templateInfo, cu, deploymentInfo);
    myCalculator.addOperation(multiply);
    
    return myCalculator;
  }
  
  private void deployTemplates(DXManServiceTemplate... templates) {
    
    for(DXManServiceTemplate template: templates) {
      deploymentManager.deployServiceTemplate(template);
    }
  }
  
  // TODO This method should be in DXManDataDeployer
  private void deployDataPipes(DXManAtomicServiceTemplate calculator) {
    
    DXManDataSpace dataSpace = DXManDataSpaceFactory.createBlockchainManager(
      "http://localhost:3000"
    );
    DXManDataDeployer dataDeployer = new DXManDataDeployer(dataSpace);
    String writerId = "Node1";
    List<String> readerIds = Arrays.asList("098ab66f-59d5-434b-b344-2b3b578796fb", "Node3");
    
    // Data pipes for Calculator
    System.out.println("Deploying data for " + calculator.getInfo().getName());
    DXManOperation multiply = calculator.getOperations().get("multiply");
    System.out.println("Data pipe for N1");
    dataDeployer.deployDataPipe(new DXManDataPipe(multiply.getParameters().get("n1").getId(), writerId, readerIds));
    System.out.println("Data pipe for N2");
    dataDeployer.deployDataPipe(new DXManDataPipe(multiply.getParameters().get("n2").getId(), writerId, readerIds));
    System.out.println("Data pipe for RESULT");
    dataDeployer.deployDataPipe(new DXManDataPipe(multiply.getParameters().get("result").getId(), writerId, readerIds));
  }
  
  private DXManCompositeServiceTemplate designSeqComposite(int seqNum, DXManServiceTemplate... templates) {
    
    DXManSequencerTemplate sequencer = new DXManSequencerTemplate();
    sequencer.composeServices(templates);
    
    DXManServiceInfo templateInfo = new DXManServiceInfo("SEQ"+seqNum, "Category Testing", 892.5);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("192.168.0.5", 5683);    
    DXManCompositeServiceTemplate composite = new DXManCompositeServiceTemplate(templateInfo, sequencer, deploymentInfo);
    
    return composite;
  }
  
  private DXManCompositeServiceTemplate designParallelComposite(int parNum, DXManServiceTemplate... templates) {
    
    DXManParallelTemplate parallel = new DXManParallelTemplate(DXManParallelType.SYNC);
    parallel.composeServices(templates);
    
    DXManServiceInfo templateInfo = new DXManServiceInfo("PAR"+parNum, "Category Testing", 892.5);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("192.168.0.5", 5683);    
    DXManCompositeServiceTemplate composite = new DXManCompositeServiceTemplate(templateInfo, parallel, deploymentInfo);
    
    return composite;
  }
    
  public static void main(String[] args) throws URISyntaxException {
    
    DesignerMultilevel designer = new DesignerMultilevel();
    
    // Designs and deploys atomic services
    
    DXManAtomicServiceTemplate ledTemplate1 = designer.designAtomicLed(1);
    DXManAtomicServiceTemplate ledTemplate2 = designer.designAtomicLed(2);
    DXManAtomicServiceTemplate ledTemplate3 = designer.designAtomicLed(3);
    
    DXManAtomicServiceTemplate calcTemplate1 = designer.designAtomicCalculator(1);
    DXManAtomicServiceTemplate calcTemplate2 = designer.designAtomicCalculator(2);
    
    designer.deployTemplates(
      ledTemplate1, ledTemplate2, ledTemplate3, 
      calcTemplate1, calcTemplate2
    );
    
    designer.deployDataPipes(calcTemplate1);
    designer.deployDataPipes(calcTemplate2);
        
    // Designs and deploys the composite service
    
    DXManCompositeServiceTemplate seq0 = designer.designSeqComposite(
      0, ledTemplate3, calcTemplate2
    );
    DXManCompositeServiceTemplate par0 = designer.designParallelComposite(
      0, ledTemplate3, calcTemplate2
    );
    DXManCompositeServiceTemplate seq1 = designer.designSeqComposite(
      1, calcTemplate1, seq0
    );
    DXManCompositeServiceTemplate seq2 = designer.designSeqComposite(
      2, seq1, par0
    );
    
    designer.deployTemplates(seq0,par0, seq1, seq2);
    
  }
}
