package esp8266composite;

import com.dxman.execution.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONException;

/** 
 * TODO Change the design of operations: it should be an IP per operation
 * @author Damian Arellanes
 */
public class ClientComposite {
    
  private static final String CALCULATOR_CONNECTOR = "MyCalculator";
  private static final String N1_ID = "4ee19e1c-213c-4c10-9f7f-2f207fcc0d74";
  private static final String N1_VALUE = "9";
  private static final String N2_ID = "7e4a7a1d-52a6-472e-9fa4-ee5bab467369";
  private static final String N2_VALUE = "100";  
  private static final String RESULT_ID = "4ecf6053-bedc-439b-aa64-8f5fc8fb4a30";
  
  private static final String LED_CONNECTOR = "LedService";
  
  private static final String SEQ_CONNECTOR = "MyComposite";
  
  public static DXManWfSpec simpleWf() throws JSONException {
        
    DXManWfInvocation led = new DXManWfInvocation(
      "on", "coap://192.168.0.5:5683/" + LED_CONNECTOR
    );
    DXManWfInvocation multiply = new DXManWfInvocation(
      "multiply", "coap://192.168.0.5:5683/" + CALCULATOR_CONNECTOR
    );
    
    DXManWfSequencer seq0 = new DXManWfSequencer(
      "seq0", "coap://192.168.0.5:5683/" + SEQ_CONNECTOR
    );  
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(led, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(0)))
      )
    );
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(multiply, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(1)))
      )
    );
    seq0.finishSequence();

    return new DXManWfSpec("wf1", seq0);
  }
  
  public static void main(String[] args) throws JSONException {
    
    DXManWorkflowManager wfManager = new DXManWorkflowManager();
    DXManWorkflowInputs wfInputs = new DXManWorkflowInputs();
    wfInputs.put(N1_ID, N1_VALUE);
    wfInputs.put(N2_ID, N2_VALUE);
    DXManWorkflowOutputs wfOutputs = new DXManWorkflowOutputs();
    wfOutputs.add(RESULT_ID);
    
    DXManWorkflowResult result = wfManager.executeWorkflow(simpleWf(), wfInputs, wfOutputs
    );
    
    result.forEach((outputId, outputVal) -> {    
      System.out.println(outputId + " --> " + outputVal);
    });
  }    
}
