package com.dxman.autonomicsamples.smarthome;

/**
 * @author Damian Arellanes
 */
public class Optimizer {
  
  // Context variables
  private boolean userAtHome; // True or false
  private double energyCost; // Dollars per watt  
  private double threshold; // Threshold of expenses per day in dollars

  public Optimizer() {
  }
  
  public void setContext(boolean userAtHome, double energyCost, double threshold) {
    this.userAtHome = userAtHome;
    this.energyCost = energyCost;
    this.threshold = threshold;
  }
  
  public WorkflowVariation findBestWorkflow(WorkflowVariation... wfv) {
    
    WorkflowVariation bestWf = null;
    double max = 0.0;
    for(WorkflowVariation v: wfv) {
      
      double wfUtility = computeUtilityFor(v);
      
      if(wfUtility > max) {
        max = wfUtility;
        bestWf = v;
      }
    }
    
    return bestWf;
  }
  
  public double computeUtilityFor(WorkflowVariation wfv) {
    
    // User at home, energy cost and threshold have the same priority
    
    double w1 = 1;
    double fitnessFuncUser = computeUserAtHomeUtility(wfv); // f1
    double w2 = 1;
    double fitnessFuncEnergy = computeEnergyCostUtility(wfv); // f2
    double w3 = 1;
    double fitnessFuncTidiness = computeTidinessUtility(wfv); // f3
    
    return (w1 * fitnessFuncUser + w2 * fitnessFuncEnergy + w3 * fitnessFuncTidiness) / (w1 + w2 + w3);
  }
  
  private double computeUserAtHomeUtility(WorkflowVariation wfv) {
    
    double userAtHomeVal = this.userAtHome ? 1.0: 0.0;
    return 1 - Math.abs(userAtHomeVal - wfv.getProperties().getValueFor("userPresence"));
  }
  
  // Minimize energy cost
  private double computeEnergyCostUtility(WorkflowVariation wfv) {
    
    double wfvEnergyCost = wfv.getProperties().getValueFor("energyConsumption") * this.energyCost;
    
    if(wfvEnergyCost >= this.threshold) {
      return 0.0;
      //return (threshold-wfvEnergyCost)/threshold;
    } else {
      return 1 - (wfvEnergyCost / this.threshold);
    }
  }
  
  // Maximise tidiness
  private double computeTidinessUtility(WorkflowVariation wfv) {
    return wfv.getProperties().getValueFor("tidinessSupport");
  }  
}
