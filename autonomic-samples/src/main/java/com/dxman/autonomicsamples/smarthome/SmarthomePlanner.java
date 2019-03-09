package com.dxman.autonomicsamples.smarthome;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;
import org.moeaframework.Executor;
import org.moeaframework.core.*;
import org.moeaframework.core.variable.EncodingUtils;

/**
 * @author Damian Arellanes
 */
public class SmarthomePlanner extends Observable implements Observer {
  
  private JSONObject contextModel = new JSONObject();
  
  public SmarthomePlanner() {}
  
  @Override
  public void update(Observable o, Object contextModel) {

    try {
      if(!((JSONObject) contextModel).getString("threshold").equals("")) {
        this.contextModel = (JSONObject) contextModel;
        //System.out.println("Received PLANNER: " + this.contextModel);

        findPlan();
      }
    } catch (JSONException ex) {}
  }
  
  private Optimizer defineOptimizer() {
    
    Optimizer optimizer = new Optimizer();
    
    try {
      
      // Context models
      //optimizer.setContext(false, 0.00014, 0.2); // User, expensive energy and threshold=0.2 --> wasd|hoover; normal price
      //optimizer.setContext(true, 0.00007, 0.6); // User, expensive energy and threshold=0.6 --> washc|washd|cook|hoover; 50% discount
      //optimizer.setContext(true, 0.00012, 0.3); // User, expensive energy and threshold=0.3 --> cook; a bit cheaper than normal
      //optimizer.setContext(false, 0.00013, 0.5); // No user, cheap energy and threshold=0.5 --> washc|washd|hoover
      
      boolean userPresence = contextModel.getString("userPresence").equals("1");
      double energyCost = contextModel.getDouble("energyCost");
      double threshold = contextModel.getDouble("threshold");
      
      optimizer.setContext(userPresence, energyCost, threshold);
      
    } catch (JSONException ex) { System.out.println(ex); }
    
    return optimizer;
  }
  
  private void findPlan() {    
    
    SmarthomePlannerNSGAII converter = new SmarthomePlannerNSGAII();
    converter.setOptimizer(defineOptimizer());
    
    NondominatedPopulation result = new Executor()
      .withProblemClass(SmarthomePlannerNSGAII.class)
      .withAlgorithm("NSGAII")
      .withMaxEvaluations(10000)
      .distributeOnAllCores()
      .run();
    
    for (int i = 0; i < result.size(); i++) {
      Solution solution = result.get(i);
      double objective = solution.getObjective(0);
      
      // negate objectives to return them to their maximized form
      objective = objective * -1;
      
      System.out.println("------------------------------------------------");
      System.out.println("Solution " + (i+1) + ":");
      System.out.println("     Utility: " + objective);
      System.out.println("     Binary String: " + solution.getVariable(0));
      System.out.println("     " + converter.convertBinaryIntoWf(EncodingUtils.getBinary(solution.getVariable(0))));
      System.out.println("------------------------------------------------");
      
      JSONObject plan = new JSONObject();
      try {
        
        plan.put("date", contextModel.getString("day"));
        plan.put("chromosome", solution.getVariable(0));
        plan.put("utility", objective);
        
        setChanged();
        notifyObservers(plan);
        
        SmarthomeKnowledge.yesterdayPlan = plan;
      } catch (JSONException ex) { System.out.println(ex); }
    }
  }
}
