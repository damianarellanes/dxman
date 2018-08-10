package esp8266composite;

import com.dxman.execution.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class ClientCompositeParallel {
  
  private static final String PARALLEL_CONNECTOR = "ParallelComposite";
  
  private static final String CALC1_CONNECTOR = "Calculator1";  
  private static final String N1_ID_1 = "2542e4b7-53f5-47de-9fa2-ef6f7c20286c";
  private static final String N1_VALUE_1 = "5500";
  private static final String N2_ID_1 = "e208d43d-12e9-4754-b198-94625b197a61";
  private static final String N2_VALUE_1 = "2000";  
  private static final String RESULT_ID_1 = "df94f82c-5678-4bef-997d-d63964f4e8a3";
  
  private static final String CALC2_CONNECTOR = "Calculator2";
  private static final String N1_ID_2 = "1ca9a13a-a521-4325-9420-c0c6b4404996";
  private static final String N1_VALUE_2 = "1000";
  private static final String N2_ID_2 = "c1112fd3-5934-4c16-a75e-74a2a15e45e0";
  private static final String N2_VALUE_2 = "1000";  
  private static final String RESULT_ID_2 = "791cca6c-1c78-49fb-a211-659467092704";
  
  public static DXManWfSpec parallelWf() throws JSONException {  
        
    // Defines the subnodes for the workflow tree
    DXManWfInvocation multiply1 = new DXManWfInvocation(
      "multiply", "coap://192.168.0.5:5683/" + CALC1_CONNECTOR            
    );
    DXManWfInvocation multiply2 = new DXManWfInvocation(
      "multiply", "coap://192.168.0.5:5683/" + CALC2_CONNECTOR
    );
    
    // Defines a parallel workflow
    DXManWfParallel par0 = new DXManWfParallel("par0", "coap://192.168.0.5:5683/" + PARALLEL_CONNECTOR);    
    par0.getSubnodeMappers().add(new DXManWfNodeMapper(multiply1, new DXManWfParallelCustom(3)));
    par0.getSubnodeMappers().add(new DXManWfNodeMapper(multiply2, new DXManWfParallelCustom(2)));
        
    return new DXManWfSpec("wf1", par0);
  }
  
  public static void main(String[] args) throws JSONException {
    
    DXManWorkflowManager wfManager = new DXManWorkflowManager();    
    DXManWorkflowData wfInputs = new DXManWorkflowData();
    wfInputs.put(N1_ID_1, N1_VALUE_1);
    wfInputs.put(N2_ID_1, N2_VALUE_1);
    wfInputs.put(N1_ID_2, N1_VALUE_2);
    wfInputs.put(N2_ID_2, N2_VALUE_2);
    DXManWorkflowOutputs wfOutputs = new DXManWorkflowOutputs();
    wfOutputs.add(RESULT_ID_1);
    wfOutputs.add(RESULT_ID_2);
    
    DXManWorkflowData result = wfManager.executeWorkflow(parallelWf(), wfInputs, wfOutputs);
    
    result.forEach((outputId, outputVal) -> {    
      System.out.println(outputId + " --> " + outputVal);
    });
  }    
}
