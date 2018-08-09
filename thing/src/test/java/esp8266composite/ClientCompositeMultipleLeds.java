package esp8266composite;

import com.dxman.execution.*;
import org.json.JSONException;

/**
 * TODO Change impl of ESP8266 to have two operations: on and off
 * TODO Change the design of operations: it should be an IP per operation
 * @author Damian Arellanes
 */
public class ClientCompositeMultipleLeds {
  
  private static final String LED1_CONNECTOR = "LedService1";
  private static final String STATUS1_ID = "ade735af-0bd2-4bb7-becc-832ec872c74c";
  private static final String STATUS1_VALUE = "off";
  
  private static final String LED2_CONNECTOR = "LedService2";
  private static final String STATUS2_ID = "ff94987a-ef3e-460b-960a-20a762d19d79";
  private static final String STATUS2_VALUE = "on";
  
  private static final String LED3_CONNECTOR = "LedService3";
  private static final String STATUS3_ID = "43505def-1e5d-4a96-a342-89c7ccf1d284";
  private static final String STATUS3_VALUE = "on";
  
  private static final String SEQ_CONNECTOR = "MyComposite";
  
  public static DXManWfSpec simpleWf() throws JSONException {  
        
    DXManWfInvocation led1 = new DXManWfInvocation(
      "led", "coap://192.168.0.5:5683/" + LED1_CONNECTOR
    );
    DXManWfInvocation led2 = new DXManWfInvocation(
      "led", "coap://192.168.0.5:5683/" + LED2_CONNECTOR
    );
    /*DXManWfInvocation led3 = new DXManWfInvocation(
      "led", "coap://192.168.0.5:5683/" + LED3_CONNECTOR
    );*/
    
    DXManWfSequencer seq0 = new DXManWfSequencer(
      "seq0", "coap://192.168.0.5:5683/" + SEQ_CONNECTOR
    );
    seq0.getSubnodes().add(led1);
    seq0.getSubnodes().add(led2);
    //seq0.getSubnodes().add(led3);

    return new DXManWfSpec("wf1", seq0);
  }
  
  public static void main(String[] args) throws JSONException {
    
    DXManWorkflowManager wfManager = new DXManWorkflowManager();
    DXManWorkflowData wfInputs = new DXManWorkflowData();
    wfInputs.put(STATUS1_ID, STATUS1_VALUE);
    wfInputs.put(STATUS2_ID, STATUS2_VALUE);
    DXManWorkflowOutputs wfOutputs = new DXManWorkflowOutputs();
    
    while(true) {
      wfManager.executeWorkflow(simpleWf(), wfInputs, wfOutputs);
    }
    
    
  }    
}
