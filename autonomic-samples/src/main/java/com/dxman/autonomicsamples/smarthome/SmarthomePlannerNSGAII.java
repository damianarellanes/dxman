package com.dxman.autonomicsamples.smarthome;

import java.util.ArrayList;
import org.moeaframework.core.*;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

/**
 * @author Damian Arellanes
 */
public class SmarthomePlannerNSGAII extends AbstractProblem {
  
  public static Optimizer optimizer;

  public SmarthomePlannerNSGAII() {
    super(1, 1);
  }
  
  @Override
  public Solution newSolution() {
    
    // 1 variable (wf variation with nOperations bits) and 1 utility function
    Solution solution = new Solution(1, 1); 
    solution.setVariable(0, EncodingUtils.newBinary(SmarthomeKnowledge.operations.length));
    
    return solution;
  }

  // We maximise the overall utility of a workflow variant
  @Override
  public void evaluate(Solution solution) {
    
    boolean[] binaryWf = EncodingUtils.getBinary(solution.getVariable(0));    
    
    if(validWorkflowVariation(binaryWf)) {
      
      WorkflowVariation wfVariation = convertBinaryIntoWf(binaryWf);
    
      double utility = optimizer.computeUtilityFor(wfVariation);

      //System.out.println(solution.getVariable(0) + " ---> " + utility);

      // MOEA Framework is designed to minimize objectives
      // So, negate the objective function value to maximize the utility
      solution.setObjective(0, utility * -1);
    } else solution.setObjective(0, 0); // Utility=0 for invalid solutions
  }
  
  private boolean validWorkflowVariation(boolean[] binaryWf) {
    
    // The empty space is not allowed in a parallel: 0000 is not allowed
    if(!binaryWf[0] && !binaryWf[1] && !binaryWf[2] && !binaryWf[3]) return false;
    
    return true;    
  }
  
  public WorkflowVariation convertBinaryIntoWf(boolean[] binaryWf) {    
    
    ArrayList<Operation> wfOps = new ArrayList<>();
    for (int i = 0; i < SmarthomeKnowledge.operations.length; i++) {
      if (binaryWf[i]) wfOps.add(SmarthomeKnowledge.operations[i]);
    }
    
    Operation[] o = new Operation[wfOps.size()];
    o = wfOps.toArray(o);
    return new WorkflowVariation(o);  
  }

  public void setOptimizer(Optimizer optimizer) { this.optimizer = optimizer; }
}

