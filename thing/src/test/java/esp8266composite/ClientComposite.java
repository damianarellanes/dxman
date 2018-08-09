package esp8266composite;

import com.dxman.execution.*;
import org.json.JSONException;

/**
 * TODO Change impl of ESP8266 to have two operations: on and off
 * TODO Change the design of operations: it should be an IP per operation
 * @author Damian Arellanes
 */
public class ClientComposite {
    
  private static final String CALCULATOR_CONNECTOR = "MyCalculator";
  private static final String N1_ID = "0c5188a7-a1c8-491a-8aae-d016c9801adf";
  private static final String N1_VALUE = "3";
  private static final String N2_ID = "242543fb-6aaf-469b-ac37-bb743cb99e86";
  private static final String N2_VALUE = "20";  
  private static final String RESULT_ID = "79eab66a-2846-46a3-9482-61dd6142a564";
  
  private static final String LED_CONNECTOR = "LedService";
  private static final String STATUS_ID = "39906327-073a-448a-95c7-5d6b8f2e6b95";
  private static final String STATUS_VALUE = "on";
  
  private static final String SEQ_CONNECTOR = "MyComposite";
  
  public static DXManWfSpec simpleWf() throws JSONException {
        
    DXManWfInvocation led = new DXManWfInvocation(
      "led", "coap://192.168.0.5:5683/" + LED_CONNECTOR
    );
    DXManWfInvocation multiply = new DXManWfInvocation(
      "multiply", "coap://192.168.0.5:5683/" + CALCULATOR_CONNECTOR
    );
    
    DXManWfSequencer seq0 = new DXManWfSequencer(
      "seq0", "coap://192.168.0.5:5683/" + SEQ_CONNECTOR
    );
    seq0.getSubnodes().add(led);
    seq0.getSubnodes().add(multiply);

    return new DXManWfSpec("wf1", seq0);
  }
  
  public static void main(String[] args) throws JSONException {
    
    DXManWorkflowManager wfManager = new DXManWorkflowManager();
    DXManWorkflowData wfInputs = new DXManWorkflowData();
    wfInputs.put(N1_ID, N1_VALUE);
    wfInputs.put(N2_ID, N2_VALUE);
    wfInputs.put(STATUS_ID, STATUS_VALUE);
    DXManWorkflowOutputs wfOutputs = new DXManWorkflowOutputs();
    wfOutputs.add(RESULT_ID);
    
    DXManWorkflowData result = wfManager.executeWorkflow(simpleWf(), wfInputs, wfOutputs
    );
    
    result.forEach((outputId, outputVal) -> {    
      System.out.println(outputId + " --> " + outputVal);
    });
  }    
}
