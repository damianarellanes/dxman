package esp8266composite;

import com.dxman.execution.*;
import org.json.JSONException;

/** 
 * TODO Change the design of operations: it should be an IP per operation
 * @author Damian Arellanes
 */
public class ClientComposite extends DXManWfTreeSeq {
    
  private static final String CALCULATOR_CONNECTOR = "MyCalculator";
  private static final String N1_ID = "4ee19e1c-213c-4c10-9f7f-2f207fcc0d74";
  private static final String N1_VALUE = "9";
  private static final String N2_ID = "7e4a7a1d-52a6-472e-9fa4-ee5bab467369";
  private static final String N2_VALUE = "100";  
  private static final String RESULT_ID = "4ecf6053-bedc-439b-aa64-8f5fc8fb4a30";
  
  private static final String LED_CONNECTOR = "LedService";
  
  private static final String SEQ_CONNECTOR = "MyComposite";
  
  public ClientComposite(String id, String uri, DXManWfTree... subWorkflows) {
    super(id, uri, subWorkflows);
  }
  
  @Override
  public void design() {
        
    DXManWfInvocation led = new DXManWfInvocation(
      "on", "coap://192.168.0.5:5683/" + LED_CONNECTOR
    );
    DXManWfInvocation multiply = new DXManWfInvocation(
      "multiply", "coap://192.168.0.5:5683/" + CALCULATOR_CONNECTOR
    );
    
    composeWf(led, 0);
    composeWf(multiply, 1);
  }
  
  public static void main(String[] args) throws JSONException {
    
    DXManWfManager wfManager = new DXManWfManager();
    
    ClientComposite seq0 = new ClientComposite("seq0", "coap://192.168.0.5:5683/MyComposite");
    seq0.execute(wfManager).forEach((outputId, outputVal) -> {    
      System.out.println(outputId + " --> " + outputVal);
    });
  }    

  @Override
  public DXManWfInputs getInputs() {
    DXManWfInputs wfInputs = new DXManWfInputs();
    wfInputs.put(N1_ID, N1_VALUE);
    wfInputs.put(N2_ID, N2_VALUE);
    return wfInputs;
  }

  @Override
  public DXManWfOutputs getOutputs() {
    DXManWfOutputs wfOutputs = new DXManWfOutputs();
    wfOutputs.add(RESULT_ID);
    return wfOutputs;
  }
}
