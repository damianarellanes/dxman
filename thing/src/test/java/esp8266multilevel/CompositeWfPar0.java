package esp8266multilevel;

import com.dxman.execution.invocation.DXManWfInvocation;
import com.dxman.execution.DXManWorkflowTreePar;
import com.dxman.execution.wttree.DXManWfInputs;
import com.dxman.execution.wttree.DXManWfOutputs;
import com.dxman.execution.DXManWorkflowTreeInv;

/**
 * @author Damian Arellanes
 */
public class CompositeWfPar0 extends DXManWorkflowTreePar {
  
  private final String LED3_CONNECTOR = "LedService3";
  private final String CALC2_CONNECTOR = "Calculator2";
  private final String N1_ID_2 = "c704080f-dd34-4e9c-9fbf-860c2a7c35bf";
  private final String N1_VALUE_2 = "10";
  private final String N2_ID_2 = "336b2747-f6cc-4a08-b152-02000cccbb3d";
  private final String N2_VALUE_2 = "8";  
  private final String RESULT_ID_2 = "d81166fb-340c-4ba4-bc00-edc60967ab52";
  
  public CompositeWfPar0(String id, String uri) {
    super(id, uri);
  }

  @Override
  public void design() {  
        
    // Defines the subnodes for the workflow tree
    DXManWorkflowTreeInv on3 = new DXManWorkflowTreeInv(
      "on", "coap://192.168.0.5:5683/" + LED3_CONNECTOR
    );
    DXManWorkflowTreeInv off3 = new DXManWorkflowTreeInv(
      "off", "coap://192.168.0.5:5683/" + LED3_CONNECTOR
    );
    DXManWorkflowTreeInv multiply2 = new DXManWorkflowTreeInv(
      "multiply", "coap://192.168.0.5:5683/" + CALC2_CONNECTOR
    );
    
    composeWf(on3, 1);
    composeWf(multiply2, 5);
  }
  
  @Override
  public DXManWfInputs getInputs() {
    DXManWfInputs wfInputs = new DXManWfInputs();
    wfInputs.put(N1_ID_2, N1_VALUE_2);
    wfInputs.put(N2_ID_2, N2_VALUE_2);
    return wfInputs;
  }
  
  @Override
  public DXManWfOutputs getOutputs() {
    DXManWfOutputs wfOutputs = new DXManWfOutputs();
    wfOutputs.add(RESULT_ID_2);
    return wfOutputs;
  }
}
