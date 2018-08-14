package leds;

import com.dxman.execution.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class User {
  
  public static DXManWorkflowResult executeSeq0(DXManWorkflowManager wfManager) throws JSONException {
    
    return wfManager.executeWorkflow(
      CompositeWfSeq0.getWf(), 
      CompositeWfSeq0.getInputs(), CompositeWfSeq0.getOutputs()
    );
  }
  
  public static DXManWorkflowResult executePar0(DXManWorkflowManager wfManager) throws JSONException {
    
    return wfManager.executeWorkflow(
      CompositeWfPar0.getWf(), 
      CompositeWfPar0.getInputs(), CompositeWfPar0.getOutputs()
    );
  }
  
  public static DXManWorkflowResult executeSeq1(DXManWorkflowManager wfManager) throws JSONException {
    
    return wfManager.executeWorkflow(
      CompositeWfSeq1.getWf(), 
      CompositeWfSeq1.getInputs(), CompositeWfSeq1.getOutputs()
    );
  }
  
  public static void main(String[] args) throws JSONException {
    
    DXManWorkflowManager wfManager = new DXManWorkflowManager();        
    
    executeSeq1(wfManager).forEach((outputId, outputVal) -> {    
      System.out.println(outputId + " --> " + outputVal);
    });
  }    
}
