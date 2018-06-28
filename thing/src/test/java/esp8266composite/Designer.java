package esp8266composite;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.dataspace.base.DXManDataSpaceFactory;
import com.dxman.deployment.common.DXManDeploymentManager;
import com.dxman.deployment.data.DXManDataDeployer;
import com.dxman.design.data.*;
import com.dxman.design.distribution.*;
import com.dxman.design.services.atomic.*;
import com.dxman.design.services.common.*;
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
    DXManServiceInfo templateInfo = new DXManServiceInfo("LedService", "Category ESP8266", 86.79);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("192.168.0.5", 5683);
    DXManAtomicServiceTemplate ledServiceTemplate = new DXManAtomicServiceTemplate(templateInfo, cu, deploymentInfo);
    ledServiceTemplate.addOperation(ledOperation);
    
    return ledServiceTemplate;
  }
  
  private void deployAtomicTemplate(DXManAtomicServiceTemplate atomicTemplate) {
    
    deploymentManager.deployServiceTemplate(atomicTemplate);
  }
  
  // TODO This method should be in DXManDataDeployer
  private void deployDataPipes(DXManAtomicServiceTemplate template) {
    
    DXManDataSpace dataSpace = DXManDataSpaceFactory.createBlockchainManager(
      "http://localhost:3000"
    );
    DXManDataDeployer dataDeployer = new DXManDataDeployer(dataSpace);
    
    DXManOperation ledOperation = template.getOperations().get("led");
    
    // Data pipes    
    String writerId = "Node1";
    List<String> readerIds = Arrays.asList("d7b3a626-6818-4475-bcd3-3e5ebbe1c6a0", "Node3");
    dataDeployer.deployDataPipe(new DXManDataPipe(ledOperation.getParameters().get("status").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(ledOperation.getParameters().get("return_value").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(ledOperation.getParameters().get("id").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(ledOperation.getParameters().get("name").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(ledOperation.getParameters().get("hardware").getId(), writerId, readerIds));
    dataDeployer.deployDataPipe(new DXManDataPipe(ledOperation.getParameters().get("connected").getId(), writerId, readerIds));
  }
    
  public static void main(String[] args) throws URISyntaxException {
    
    Designer designer = new Designer();
    
    DXManAtomicServiceTemplate ledTemplate = designer.designAtomicLed();
    designer.deployAtomicTemplate(ledTemplate);
    designer.deployDataPipes(ledTemplate);
  }
}
