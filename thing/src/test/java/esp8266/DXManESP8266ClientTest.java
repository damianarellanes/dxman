package esp8266;

import com.dxman.execution.DXManWfInvocation;
import com.dxman.execution.DXManWfNode;
import com.dxman.execution.DXManWfSpec;
import com.dxman.utils.RuntimeTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static esp8266.DXManESP8266ServerTest.dxmanRuntime;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import org.eclipse.californium.core.CoapClient;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class DXManESP8266ClientTest {
  
  private static String invConnector = "LedService";
  private static String paramStatusId = "82ab033d-e023-4445-b4a1-35a645c2b936";
  
  public static void setupInputValues() {      
    dxmanRuntime.getDataSpace().writeParameter(
      paramStatusId, "off"
    );
  }
   
    public static DXManWfSpec createWorkflowSpec() throws JSONException {
        
        DXManWfInvocation led = new DXManWfInvocation(
            "led", "coap://192.168.0.5:5683/" + invConnector
        );
                
        return new DXManWfSpec("wf1", led);
    }
    
    public static void main(String[] args) throws URISyntaxException, MalformedURLException, JSONException {
      
      setupInputValues();
        
        RuntimeTypeAdapterFactory<DXManWfNode> adapter = RuntimeTypeAdapterFactory
           .of(DXManWfNode.class, "wfnode")
           .registerSubtype(DXManWfInvocation.class, "wfinvocation");
        
        DXManWfSpec wfSpec = createWorkflowSpec(); 
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().registerTypeAdapterFactory(adapter).create();
        
        new CoapClient(wfSpec.getFlow().getUri()).post(gson.toJson(wfSpec.getFlow()), 0);
    }
}
