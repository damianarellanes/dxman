package esp8266multilevel;

import com.dxman.execution.DXManWfInvocation;
import com.dxman.execution.DXManWfNodeMapper;
import com.dxman.execution.DXManWfParallel;
import com.dxman.execution.DXManWfParallelCustom;
import com.dxman.execution.DXManWfSpec;
import com.dxman.execution.DXManWorkflowInputs;
import com.dxman.execution.DXManWorkflowOutputs;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class CompositeWfPar0 {
  
  private static final String PAR0 = "PAR0";
  private static final String LED3_CONNECTOR = "LedService3";
  private static final String CALC2_CONNECTOR = "Calculator2";
  private static final String N1_ID_2 = "c704080f-dd34-4e9c-9fbf-860c2a7c35bf";
  private static final String N1_VALUE_2 = "10";
  private static final String N2_ID_2 = "336b2747-f6cc-4a08-b152-02000cccbb3d";
  private static final String N2_VALUE_2 = "8";  
  private static final String RESULT_ID_2 = "d81166fb-340c-4ba4-bc00-edc60967ab52";
    
  public static DXManWfSpec getWf() throws JSONException {  
        
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
    
    // Defines a parallel workflow
    DXManWfParallel par0 = new DXManWfParallel("par0", "coap://192.168.0.5:5683/" + PAR0);    
    par0.getSubnodeMappers().add(new DXManWfNodeMapper(on3, new DXManWfParallelCustom(1)));
    par0.getSubnodeMappers().add(new DXManWfNodeMapper(multiply2, new DXManWfParallelCustom(5)));
        
    return new DXManWfSpec("wf3", par0);
  }
  
  public static DXManWorkflowInputs getInputs() {
    DXManWorkflowInputs wfInputs = new DXManWorkflowInputs();
    wfInputs.put(N1_ID_2, N1_VALUE_2);
    wfInputs.put(N2_ID_2, N2_VALUE_2);
    return wfInputs;
  }
  
  public static DXManWorkflowOutputs getOutputs() {
    DXManWorkflowOutputs wfOutputs = new DXManWorkflowOutputs();
    wfOutputs.add(RESULT_ID_2);
    return wfOutputs;
  }
}
