package esp8266composite;

import com.dxman.deployment.common.DXManDeploymentManager;
import com.dxman.design.connectors.composition.DXManSequencerTemplate;
import com.dxman.design.data.*;
import com.dxman.design.distribution.*;
import com.dxman.design.services.atomic.*;
import com.dxman.design.services.common.*;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import java.net.*;

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
    
  public static void main(String[] args) throws URISyntaxException {
    
    DesignerMultipleLeds designer = new DesignerMultipleLeds();
    
    // Designs and deploys atomic services
    
    DXManAtomicServiceTemplate ledTemplate1 = designer.designAtomicLed(1);
    DXManAtomicServiceTemplate ledTemplate2 = designer.designAtomicLed(2);
    DXManAtomicServiceTemplate ledTemplate3 = designer.designAtomicLed(3);
    
    designer.deployTemplates(ledTemplate1, ledTemplate2, ledTemplate3);
        
    // Designs and deploys the composite service
    
    DXManCompositeServiceTemplate composite = designer.designSeqComposite(
      ledTemplate1, ledTemplate2, ledTemplate3
    );
    designer.deployTemplates(composite);
    
  }
}
