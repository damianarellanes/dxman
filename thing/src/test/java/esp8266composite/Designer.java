package esp8266composite;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.dataspace.base.DXManDataSpaceFactory;
import com.dxman.deployment.common.DXManDeploymentManager;
import com.dxman.deployment.data.DXManDataDeployer;
import com.dxman.design.connectors.composition.DXManSequencerTemplate;
import com.dxman.design.data.*;
import com.dxman.design.distribution.*;
import com.dxman.design.services.atomic.*;
import com.dxman.design.services.common.*;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import java.net.*;
import java.util.*;

/**
 * @author Damian Arellanes
 */
public class Designer {
  
  private final DXManDeploymentManager deploymentManager;
  
  public Designer() {
    deploymentManager = new DXManDeploymentManager();
  }
  
  private DXManAtomicServiceTemplate designAtomicLed() throws URISyntaxException {
    
    /* Designs operation "Led" with binding and parameters */
    
    // ON Operation
    DXManBindingInfo onOperationBinding = new DXManBindingInfo(
      new URI("http://192.168.0.16/led"), 
      DXManEndpointType.HTTP_GET, 
      DXManBindingContent.QUERY_STRING, 
      DXManBindingContent.APPLICATION_JSON, 
      "?params=%7B%22status%22%3A%22on%22%7D", 
      "{\"return_value\": ##return_value##, \"id\": \"##id##\", \"name\": \"##name##\", \"hardware\": \"##hardware##\", \"connected\": ##connected##}"
    );    
    DXManOperation onOperation = new DXManOperation("on", onOperationBinding);
    DXManParameter return_value = new DXManParameter("return_value", DXManParameterType.OUTPUT, "integer"); onOperation.addParameter(return_value);
    DXManParameter id = new DXManParameter("id", DXManParameterType.OUTPUT, "integer"); onOperation.addParameter(id);
    DXManParameter name = new DXManParameter("name", DXManParameterType.OUTPUT, "string"); onOperation.addParameter(name);
    DXManParameter hardware = new DXManParameter("hardware", DXManParameterType.OUTPUT, "string"); onOperation.addParameter(hardware);
    DXManParameter connected = new DXManParameter("connected", DXManParameterType.OUTPUT, "boolean"); onOperation.addParameter(connected);
    
    // OFF Operation
    DXManBindingInfo offOperationBinding = new DXManBindingInfo(
      new URI("http://192.168.0.16/led"), 
      DXManEndpointType.HTTP_GET, 
      DXManBindingContent.QUERY_STRING, 
      DXManBindingContent.APPLICATION_JSON, 
      "?params=%7B%22status%22%3A%22off%22%7D", 
      "{\"return_value\": ##return_value##, \"id\": \"##id##\", \"name\": \"##name##\", \"hardware\": \"##hardware##\", \"connected\": ##connected##}"
    );    
    DXManOperation offOperation = new DXManOperation("off", offOperationBinding);
    DXManParameter return_value_off = new DXManParameter("return_value", DXManParameterType.OUTPUT, "integer"); offOperation.addParameter(return_value_off);
    DXManParameter id_off = new DXManParameter("id", DXManParameterType.OUTPUT, "integer"); offOperation.addParameter(id_off);
    DXManParameter name_off = new DXManParameter("name", DXManParameterType.OUTPUT, "string"); offOperation.addParameter(name_off);
    DXManParameter hardware_off = new DXManParameter("hardware", DXManParameterType.OUTPUT, "string"); offOperation.addParameter(hardware_off);
    DXManParameter connected_off = new DXManParameter("connected", DXManParameterType.OUTPUT, "boolean"); offOperation.addParameter(connected_off);
    
    /*Designs the atomic service template for "LedService"*/
    DXManComputationUnit cu = new DXManComputationUnit();
    DXManServiceInfo templateInfo = new DXManServiceInfo("LedService", "Category ESP8266", 86.79);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("192.168.0.5", 5683);
    DXManAtomicServiceTemplate ledServiceTemplate = new DXManAtomicServiceTemplate(templateInfo, cu, deploymentInfo);
    ledServiceTemplate.addOperation(onOperation);
    ledServiceTemplate.addOperation(offOperation);
    
    return ledServiceTemplate;
  }
  
  private DXManAtomicServiceTemplate designAtomicCalculator() throws URISyntaxException {
    
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
    DXManServiceInfo templateInfo = new DXManServiceInfo("MyCalculator", "Category Maths", 100);
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
  
  private DXManCompositeServiceTemplate designSeqComposite(DXManServiceTemplate... templates) {
    
    DXManSequencerTemplate sequencer = new DXManSequencerTemplate();
    sequencer.composeServices(templates);
    
    DXManServiceInfo templateInfo = new DXManServiceInfo("MyComposite", "Category Testing", 892.5);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("192.168.0.5", 5683);    
    DXManCompositeServiceTemplate composite = new DXManCompositeServiceTemplate(templateInfo, sequencer, deploymentInfo);
    
    return composite;
  }
  
  // TODO This method should be in DXManDataDeployer
  private void deployDataPipes(DXManAtomicServiceTemplate led, DXManAtomicServiceTemplate calculator) {
    
    DXManDataSpace dataSpace = DXManDataSpaceFactory.createBlockchainManager(
      "http://localhost:3000"
    );
    DXManDataDeployer dataDeployer = new DXManDataDeployer(dataSpace);
    String writerId = "Node1";
    List<String> readerIds = Arrays.asList("186dc6a1-4456-4acd-821f-137478a8b7fe", "Node3");
    
    // Data pipes for Led
    /*DXManOperation onOperation = led.getOperations().get("on");
    dataDeployer.deployDataPipe(new DXManDataPipe(onOperation.getParameters().get("return_value").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(onOperation.getParameters().get("id").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(onOperation.getParameters().get("name").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(onOperation.getParameters().get("hardware").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(onOperation.getParameters().get("connected").getId(), writerId, readerIds));
    DXManOperation offOperation = led.getOperations().get("off");
    dataDeployer.deployDataPipe(new DXManDataPipe(offOperation.getParameters().get("return_value").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(offOperation.getParameters().get("id").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(offOperation.getParameters().get("name").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(offOperation.getParameters().get("hardware").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(offOperation.getParameters().get("connected").getId(), writerId, readerIds));*/
    
    // Data pipes for Calculator
    DXManOperation multiply = calculator.getOperations().get("multiply");
    System.out.println("Data pipe for N1");
    dataDeployer.deployDataPipe(new DXManDataPipe(multiply.getParameters().get("n1").getId(), writerId, readerIds));
    System.out.println("Data pipe for N2");
    dataDeployer.deployDataPipe(new DXManDataPipe(multiply.getParameters().get("n2").getId(), writerId, readerIds));
    System.out.println("Data pipe for RESULT");
    dataDeployer.deployDataPipe(new DXManDataPipe(multiply.getParameters().get("result").getId(), writerId, readerIds));
  }
    
  public static void main(String[] args) throws URISyntaxException {
    
    Designer designer = new Designer();
    
    // Designs and deploys atomic services
    
    DXManAtomicServiceTemplate ledTemplate = designer.designAtomicLed();
    DXManAtomicServiceTemplate calculatorTemplate = designer.designAtomicCalculator();
    
    designer.deployTemplates(ledTemplate, calculatorTemplate);
    designer.deployDataPipes(ledTemplate, calculatorTemplate);
        
    // Designs and deploys the composite service
    
    DXManCompositeServiceTemplate composite = designer.designSeqComposite(
      ledTemplate, calculatorTemplate
    );
    designer.deployTemplates(composite);
    
  }
}
