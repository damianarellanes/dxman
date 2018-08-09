package esp8266composite;

import com.dxman.execution.*;
import java.net.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class ClientLed {
  
  private static final String INVCONNECTOR = "LedService";
  private static final String STATUS_ID = "b3de59f9-dfd8-4fc7-8136-71c5e03433d9";
  private static final String STATUS_VALUE = "off";
  
  public static DXManWfSpec createWorkflowSpec() throws JSONException {
        
    DXManWfInvocation led = new DXManWfInvocation(
      "led", "coap://192.168.0.5:5683/" + INVCONNECTOR
    );

    return new DXManWfSpec("wf1", led);
  }
  
  public static void main(String[] args) throws URISyntaxException, MalformedURLException, JSONException {          

    DXManWorkflowManager wfManager = new DXManWorkflowManager();
    DXManWorkflowData wfInputs = new DXManWorkflowData();
    wfInputs.put(STATUS_ID, STATUS_VALUE);
    
    DXManWorkflowOutputs wfOutputs = new DXManWorkflowOutputs();
    
    wfManager.executeWorkflow(createWorkflowSpec(), wfInputs, wfOutputs);
  }
}
