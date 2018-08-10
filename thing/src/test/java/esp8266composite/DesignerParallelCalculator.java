package esp8266composite;

import com.dxman.dataspace.base.*;
import com.dxman.deployment.common.DXManDeploymentManager;
import com.dxman.deployment.data.DXManDataDeployer;
import com.dxman.design.connectors.composition.*;
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
public class DesignerParallelCalculator {
  
  private final DXManDeploymentManager deploymentManager;
  
  public DesignerParallelCalculator() {
    deploymentManager = new DXManDeploymentManager();
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
    List<String> readerIds = Arrays.asList("ec0a39e8-b93c-462d-8c3e-847d2c9a896c", "Node3");
    
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
  
  private DXManCompositeServiceTemplate designParallelComposite(DXManServiceTemplate... templates) {
    
    DXManParallelTemplate parallel = new DXManParallelTemplate(DXManParallelType.SYNC);
    parallel.composeServices(templates);
    
    DXManServiceInfo templateInfo = new DXManServiceInfo("ParallelComposite", "Category Testing", 892.5);
    DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("192.168.0.5", 5683);    
    DXManCompositeServiceTemplate composite = new DXManCompositeServiceTemplate(templateInfo, parallel, deploymentInfo);
    
    return composite;
  }
    
  public static void main(String[] args) throws URISyntaxException {
    
    DesignerParallelCalculator designer = new DesignerParallelCalculator();
    
    // Designs and deploys atomic services
    
    DXManAtomicServiceTemplate calcTemplate1 = designer.designAtomicCalculator(1);
    DXManAtomicServiceTemplate calcTemplate2 = designer.designAtomicCalculator(2);
    
    designer.deployTemplates(calcTemplate1, calcTemplate2);
    designer.deployDataPipes(calcTemplate1);
    designer.deployDataPipes(calcTemplate2);
        
    // Designs and deploys the composite service
    
    DXManCompositeServiceTemplate composite = designer.designParallelComposite(
      calcTemplate1, calcTemplate2
    );
    designer.deployTemplates(composite);
    
  }
}
