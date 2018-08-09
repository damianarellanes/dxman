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
public class DesignerMultipleLeds {
  
  private final DXManDeploymentManager deploymentManager;
  
  public DesignerMultipleLeds() {
    deploymentManager = new DXManDeploymentManager();
  }
  
  private DXManAtomicServiceTemplate designAtomicLed(int ledNumber) throws URISyntaxException {
    
    // Designs operation "Led" with binding and parameters
    
    DXManBindingInfo ledOperationBinding = new DXManBindingInfo(
      new URI("http://192.168.0.16/led"), 
      DXManEndpointType.HTTP_GET, 
      DXManBindingContent.QUERY_STRING, 
      DXManBindingContent.APPLICATION_JSON, 
      "?params=%7B%22status%22%3A%22##status##%22%7D", 
      "{\"return_value\": ##return_value##, \"id\": \"##id##\", \"name\": \"##name##\", \"hardware\": \"##hardware##\", \"connected\": ##connected##}"
    );
    
    DXManOperation ledOperation = new DXManOperation("led", ledOperationBinding);
    DXManParameter status = new DXManParameter("status", DXManParameterType.INPUT, "string"); ledOperation.addParameter(status);
    DXManParameter return_value = new DXManParameter("return_value", DXManParameterType.OUTPUT, "integer"); ledOperation.addParameter(return_value);
    DXManParameter id = new DXManParameter("id", DXManParameterType.OUTPUT, "integer"); ledOperation.addParameter(id);
    DXManParameter name = new DXManParameter("name", DXManParameterType.OUTPUT, "string"); ledOperation.addParameter(name);
    DXManParameter hardware = new DXManParameter("hardware", DXManParameterType.OUTPUT, "string"); ledOperation.addParameter(hardware);
    DXManParameter connected = new DXManParameter("connected", DXManParameterType.OUTPUT, "boolean"); ledOperation.addParameter(connected);
    
    // Designs the atomic service template for "LedService"
    DXManComputationUnit cu = new DXManComputationUnit();
    DXManServiceInfo templateInfo = new DXManServiceInfo("LedService" + ledNumber, "Category ESP8266", 86.79);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("192.168.0.5", 5683);
    DXManAtomicServiceTemplate ledServiceTemplate = new DXManAtomicServiceTemplate(templateInfo, cu, deploymentInfo);
    ledServiceTemplate.addOperation(ledOperation);
    
    return ledServiceTemplate;
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
  private void deployDataPipes(DXManAtomicServiceTemplate led) {
    
    DXManDataSpace dataSpace = DXManDataSpaceFactory.createBlockchainManager(
      "http://localhost:3000"
    );
    DXManDataDeployer dataDeployer = new DXManDataDeployer(dataSpace);
    String writerId = "Node1";
    List<String> readerIds = Arrays.asList("b0c35bf6-ede7-494d-9bf9-299fa074ad3f", "Node3");
    
    // Data pipes for Led
    DXManOperation ledOperation = led.getOperations().get("led");    
    System.out.println("Data pipe for STATUS");
    dataDeployer.deployDataPipe(new DXManDataPipe(ledOperation.getParameters().get("status").getId(), writerId, readerIds));
  }
    
  public static void main(String[] args) throws URISyntaxException {
    
    DesignerMultipleLeds designer = new DesignerMultipleLeds();
    
    // Designs and deploys atomic services
    
    DXManAtomicServiceTemplate ledTemplate1 = designer.designAtomicLed(1);
    DXManAtomicServiceTemplate ledTemplate2 = designer.designAtomicLed(2);
    DXManAtomicServiceTemplate ledTemplate3 = designer.designAtomicLed(3);
    
    designer.deployTemplates(ledTemplate1, ledTemplate2, ledTemplate3);
    designer.deployDataPipes(ledTemplate1);
    designer.deployDataPipes(ledTemplate2);
    designer.deployDataPipes(ledTemplate3);
        
    // Designs and deploys the composite service
    
    DXManCompositeServiceTemplate composite = designer.designSeqComposite(
      ledTemplate1, ledTemplate2, ledTemplate3
    );
    designer.deployTemplates(composite);
    
  }
}
