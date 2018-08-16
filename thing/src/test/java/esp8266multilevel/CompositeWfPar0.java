package esp8266multilevel;

import com.dxman.execution.DXManWfInvocation;
import com.dxman.execution.DXManWfTree;
import com.dxman.execution.DXManWfTreePar;
import com.dxman.execution.DXManWorkflowInputs;
import com.dxman.execution.DXManWorkflowOutputs;

/**
 * @author Damian Arellanes
 */
public class CompositeWfPar0 extends DXManWfTreePar {
  
  private final String LED3_CONNECTOR = "LedService3";
  private final String CALC2_CONNECTOR = "Calculator2";
  private final String N1_ID_2 = "c704080f-dd34-4e9c-9fbf-860c2a7c35bf";
  private final String N1_VALUE_2 = "10";
  private final String N2_ID_2 = "336b2747-f6cc-4a08-b152-02000cccbb3d";
  private final String N2_VALUE_2 = "8";  
  private final String RESULT_ID_2 = "d81166fb-340c-4ba4-bc00-edc60967ab52";
    
  public void design() {  
        
    // Defines the subnodes for the workflow tree
    DXManWfInvocation on3 = new DXManWfInvocation(
      "on", "coap://192.168.0.5:5683/" + LED3_CONNECTOR
    );
    DXManWfInvocation off3 = new DXManWfInvocation(
      "off", "coap://192.168.0.5:5683/" + LED3_CONNECTOR
    );
    DXManWfInvocation multiply2 = new DXManWfInvocation(
      "multiply", "coap://192.168.0.5:5683/" + CALC2_CONNECTOR
    );
    
    composeWf(on3, 1);
    composeWf(multiply2, 5);
  }
  
  @Override
  public DXManWorkflowInputs getInputs() {
    DXManWorkflowInputs wfInputs = new DXManWorkflowInputs();
    wfInputs.put(N1_ID_2, N1_VALUE_2);
    wfInputs.put(N2_ID_2, N2_VALUE_2);
    return wfInputs;
  }
  
  @Override
  public DXManWorkflowOutputs getOutputs() {
    DXManWorkflowOutputs wfOutputs = new DXManWorkflowOutputs();
    wfOutputs.add(RESULT_ID_2);
    return wfOutputs;
  }

  public CompositeWfPar0(String id, String uri, DXManWfTree... subWorkflows) {
    super(id, uri, subWorkflows);
  }
}
