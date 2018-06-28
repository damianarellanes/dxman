package esp8266;

import com.dxman.deployment.common.DXManDeploymentRequest;
import com.dxman.deployment.data.DXManDataDeployer;
import com.dxman.design.data.DXManDataPipe;
import com.dxman.design.data.DXManOperation;
import com.dxman.design.data.DXManParameter;
import com.dxman.design.data.DXManParameterType;
import com.dxman.design.distribution.DXManBindingContent;
import com.dxman.design.distribution.DXManBindingInfo;
import com.dxman.design.distribution.DXManDeploymentInfo;
import com.dxman.design.distribution.DXManEndpointType;
import com.dxman.design.services.atomic.DXManAtomicServiceTemplate;
import com.dxman.design.services.atomic.DXManComputationUnit;
import com.dxman.design.services.common.DXManServiceInfo;
import com.dxman.design.services.common.DXManServiceType;
import com.dxman.thing.runtime.DXManRuntime;
import com.dxman.thing.runtime.DXManStarter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import org.eclipse.californium.core.CoapClient;

/**
 * @author Damian Arellanes
 */
public class DXManESP8266ServerTest {
            
  public static final DXManRuntime dxmanRuntime = DXManStarter.start(
    "/home/darellanes/DX-MAN-Platform/examples/window.properties"
  );
    
    public static DXManAtomicServiceTemplate deployAtomic() throws URISyntaxException, MalformedURLException {
      
      DXManDataDeployer dataDeployer = new DXManDataDeployer(dxmanRuntime.getDataSpace());
                
        DXManBindingInfo op1Binding = new DXManBindingInfo(
            new URI("http://192.168.0.16/led"), 
            DXManEndpointType.HTTP_GET, 
            DXManBindingContent.QUERY_STRING, 
            DXManBindingContent.APPLICATION_JSON, 
            "?params=%7B%22status%22%3A%22##status##%22%7D", 
            "{\"return_value\": ##return_value##, \"id\": \"##id##\", \"name\": \"##name##\", \"hardware\": \"##hardware##\", \"connected\": ##connected##}"
        );
        
        DXManOperation op1 = new DXManOperation("led", op1Binding);
        
        DXManParameter status = new DXManParameter("status", DXManParameterType.INPUT, "string"); op1.addParameter(status);
        DXManParameter return_value = new DXManParameter("return_value", DXManParameterType.OUTPUT, "integer"); op1.addParameter(return_value);
        DXManParameter id = new DXManParameter("id", DXManParameterType.OUTPUT, "integer"); op1.addParameter(id);
        DXManParameter name = new DXManParameter("name", DXManParameterType.OUTPUT, "string"); op1.addParameter(name);
        DXManParameter hardware = new DXManParameter("hardware", DXManParameterType.OUTPUT, "string"); op1.addParameter(hardware);
        DXManParameter connected = new DXManParameter("connected", DXManParameterType.OUTPUT, "boolean"); op1.addParameter(connected);
        
        DXManComputationUnit cu = new DXManComputationUnit();
        DXManServiceInfo templateInfo = new DXManServiceInfo("LedService", "Category ESP8266", 86.79);        
        DXManDeploymentInfo deploymentInfo = new DXManDeploymentInfo("192.168.0.5", 5683);
        DXManAtomicServiceTemplate template = new DXManAtomicServiceTemplate(templateInfo, cu, deploymentInfo);
        template.addOperation(op1);
        
        // Data pipes
        String writerId = "Node1";
        List<String> readerIds = Arrays.asList(dxmanRuntime.getThing().getId(), "Node3");
        dataDeployer.deployDataPipe(new DXManDataPipe(status.getId(), writerId, readerIds));
        dataDeployer.deployDataPipe(new DXManDataPipe(return_value.getId(), writerId, readerIds));
        dataDeployer.deployDataPipe(new DXManDataPipe(id.getId(), writerId, readerIds));
        dataDeployer.deployDataPipe(new DXManDataPipe(name.getId(), writerId, readerIds));
        dataDeployer.deployDataPipe(new DXManDataPipe(hardware.getId(), writerId, readerIds));
        dataDeployer.deployDataPipe(new DXManDataPipe(connected.getId(), writerId, readerIds));
        
        return template;
    }
    
    public static void deployInvocationConnector(DXManAtomicServiceTemplate managedService) {
      
      Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
      DXManDeploymentRequest req = new DXManDeploymentRequest(DXManServiceType.ATOMIC, managedService);
      
      new CoapClient("coap://192.168.0.5:5683/deployer").post(gson.toJson(req), 0); // TODO change from deployer to deployer-UUID
    }
    
    public static void main(String[] args) throws URISyntaxException, MalformedURLException {
        
      deployInvocationConnector(deployAtomic());
    }
}
