package leds;

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
public class Designer {
  
  private final DXManDeploymentManager deploymentManager;
  
  public Designer() {
    deploymentManager = new DXManDeploymentManager();
  }
  
  private DXManAtomicServiceTemplate designAtomicLed(int ledNumber) throws URISyntaxException {
    
    // Designs operation "Led" with binding and parameters
    
    DXManBindingInfo onOperationBinding = new DXManBindingInfo(
      new URI("http://localhost:808" + (ledNumber+1) + "/led/api/on"), 
      DXManEndpointType.HTTP_GET, 
      DXManBindingContent.NO_CONTENT, 
      DXManBindingContent.PLAIN, 
      "", 
      ""
    );
    DXManBindingInfo offOperationBinding = new DXManBindingInfo(
      new URI("http://localhost:808" + (ledNumber+1) + "/led/api/off"), 
      DXManEndpointType.HTTP_GET, 
      DXManBindingContent.NO_CONTENT, 
      DXManBindingContent.PLAIN, 
      "", 
      ""
    );
    
    DXManOperation onOperation = new DXManOperation("on", onOperationBinding);
    DXManOperation offOperation = new DXManOperation("off", offOperationBinding);
    
    // Designs the atomic service template for "LedService"
    DXManComputationUnit cu = new DXManComputationUnit();
    DXManServiceInfo templateInfo = new DXManServiceInfo("IC" + ledNumber, "Examples", 0);
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
  
  private DXManCompositeServiceTemplate designSeqComposite(int seqNum, DXManServiceTemplate... templates) {
    
    DXManSequencerTemplate sequencer = new DXManSequencerTemplate();
    sequencer.composeServices(templates);
    
    DXManServiceInfo templateInfo = new DXManServiceInfo("SEQ"+seqNum, "Example", 0);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("192.168.0.5", 5683);    
    DXManCompositeServiceTemplate composite = new DXManCompositeServiceTemplate(templateInfo, sequencer, deploymentInfo);
    
    return composite;
  }
  
  private DXManCompositeServiceTemplate designParallelComposite(int parNum, DXManServiceTemplate... templates) {
    
    DXManParallelTemplate parallel = new DXManParallelTemplate(DXManParallelType.SYNC);
    parallel.composeServices(templates);
    
    DXManServiceInfo templateInfo = new DXManServiceInfo("PAR"+parNum, "Example", 0);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("192.168.0.5", 5683);    
    DXManCompositeServiceTemplate composite = new DXManCompositeServiceTemplate(templateInfo, parallel, deploymentInfo);
    
    return composite;
  }
    
  public static void main(String[] args) throws URISyntaxException {
    
    Designer designer = new Designer();
    
    // Designs and deploys atomic services
    
    DXManAtomicServiceTemplate ledTemplate0 = designer.designAtomicLed(0);
    DXManAtomicServiceTemplate ledTemplate1 = designer.designAtomicLed(1);
    DXManAtomicServiceTemplate ledTemplate2 = designer.designAtomicLed(2);
    DXManAtomicServiceTemplate ledTemplate3 = designer.designAtomicLed(3);
    
    designer.deployTemplates(
      ledTemplate0, ledTemplate1, ledTemplate2, ledTemplate3
    );
        
    // Designs and deploys the composite service
    
    DXManCompositeServiceTemplate seq0 = designer.designSeqComposite(
      0, ledTemplate0, ledTemplate1
    );
    DXManCompositeServiceTemplate par0 = designer.designParallelComposite(
      0, ledTemplate2, ledTemplate3
    );
    DXManCompositeServiceTemplate seq1 = designer.designSeqComposite(
      1, seq0, par0
    );
        
    designer.deployTemplates(seq0, par0, seq1);
    
  }
}
