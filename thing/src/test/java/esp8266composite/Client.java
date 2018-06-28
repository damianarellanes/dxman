package esp8266composite;

import com.dxman.execution.*;
import java.net.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class Client {
  
  private static final String INVCONNECTOR = "LedService";
  private static final String STATUS_ID = "f167a6b1-2572-4320-87a4-9801aae28219";
  private static final String STATUS_VALUE = "off";
  
  public static DXManWfSpec createWorkflowSpec() throws JSONException {
        
    DXManWfInvocation led = new DXManWfInvocation(
      "led", "coap://192.168.0.5:5683/" + INVCONNECTOR
    );

    return new DXManWfSpec("wf1", led);
  }
  
  public static void main(String[] args) throws URISyntaxException, MalformedURLException, JSONException {          

    DXManWorkflowManager wfManager = new DXManWorkflowManager();
    DXManWorkflowData wfData = new DXManWorkflowData();
    wfData.put(STATUS_ID, STATUS_VALUE);
    
    wfManager.executeWorkflow(createWorkflowSpec(), wfData);
  }
}
