package esp8266composite;

import com.dxman.execution.*;
import java.net.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class ClientCalculator {
  
  private static final String INVCONNECTOR = "MyCalculator";
  private static final String N1_ID = "8b35771b-6c3a-4f4e-b120-4010de692b4f";
  private static final String N1_VALUE = "9";
  private static final String N2_ID = "7befbc27-9543-402b-9338-5f8ffa4cf9d5";
  private static final String N2_VALUE = "11";
  
  private static final String RESULT_ID = "2e2bbebb-b727-41ba-a3a8-873feb64bf42";
  
  public static DXManWfSpec createWorkflowSpec() throws JSONException {
        
    DXManWfInvocation multiply = new DXManWfInvocation(
      "multiply", "coap://192.168.0.5:5683/" + INVCONNECTOR
    );

    return new DXManWfSpec("wf1", multiply);
  }
  
  public static void main(String[] args) throws URISyntaxException, MalformedURLException, JSONException {          

    DXManWfManager wfManager = new DXManWfManager();
    DXManWfInputs wfInputs = new DXManWfInputs();
    wfInputs.put(N1_ID, N1_VALUE);
    wfInputs.put(N2_ID, N2_VALUE);
    DXManWfOutputs wfOutputs = new DXManWfOutputs();
    wfOutputs.add(RESULT_ID);
    
    DXManWfResult result = wfManager.executeWorkflow(
      createWorkflowSpec(), wfInputs, wfOutputs
    );
    
    result.forEach((outputId, outputVal) -> {    
      System.out.println(outputId + " --> " + outputVal);
    });
  }
}
