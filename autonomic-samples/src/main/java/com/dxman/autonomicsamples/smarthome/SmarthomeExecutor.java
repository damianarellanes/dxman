package com.dxman.autonomicsamples.smarthome;

import com.dxman.deployment.cli.DXManWorkflowTreeEditor;
import com.dxman.execution.wttree.*;
import java.util.*;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class SmarthomeExecutor extends DXManWorkflowTreeEditor implements Observer {
  
  private final String SMARTHOME = "57593d91-5e34-4dd8-b629-9a8787aa1594";
  private final String W_CLOTHES = "60784baf-086f-442b-937e-29dc605b7bc2";
  private final String W_DISHES = "4735f974-adf5-49a2-a22a-d78450f389d6";
  private final String W_COOK = "6526554a-035a-48b4-a102-025818dd3d4b";  
  private final String W_ROBOT = "96a57586-a231-49b4-8b7e-95e118dec461";
  
  private JSONObject plan = new JSONObject();

  private final SmarthomeEffector effector;
  
  public SmarthomeExecutor(SmarthomeEffector effector) {
    
    super(SmarthomeKnowledge.abstractWfTree, "AutonomicWf", SmarthomeKnowledge.wfTreeManager.getDataspace());
    this.effector = effector;
  }
  
  
  @Override
  public void update(Observable o, Object plan) {
        
    try {
      if(!((JSONObject) plan).getString("chromosome").equals("")) {
        this.plan = (JSONObject) plan;
        System.out.println("Received EXECUTOR: " + this.plan);
        
        effector.executeConcreteWfTree(this);
      }
    } catch (JSONException ex) {}    
  }

  @Override
  public void designControl() {
    
    try {
      String chromosome = plan.getString("chromosome");
            
      customiseParallel(SMARTHOME, W_CLOTHES, Integer.valueOf(chromosome.charAt(0)));
      customiseParallel(SMARTHOME, W_DISHES, Integer.valueOf(chromosome.charAt(1)));
      customiseParallel(SMARTHOME, W_COOK, Integer.valueOf(chromosome.charAt(2)));
      customiseParallel(SMARTHOME, W_ROBOT, Integer.valueOf(chromosome.charAt(3)));
      
    } catch (JSONException ex) {}
  }

  @Override
  public void designData() {}

  @Override
  public DXManWfInputs getInputs() { return new DXManWfInputs(); }

  @Override
  public DXManWfOutputs getOutputs() { return new DXManWfOutputs(); }
}
