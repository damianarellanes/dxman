package com.dxman.autonomicsamples.smarthome;

import com.dxman.deployment.cli.DXManWorkflowTreeDesigner;
import com.dxman.execution.wttree.DXManWorkflowTree;
import org.json.JSONObject;

/**
 * @author Damian Arellanes
 */
public class SmarthomeKnowledge {

  private static String abstractWfTreePath = "/home/abstractSmarthome";
  private static String blockchainIP = "http://localhost:3000";
  public static final DXManWorkflowTreeDesigner wfTreeManager;  
  public static final DXManWorkflowTree abstractWfTree;
  
  public static JSONObject yesterdayContextModel = new JSONObject();
  public static JSONObject yesterdayPlan = new JSONObject();
  
  public static final Operation[] operations = new Operation[4];
  
  static {
    
    // Reads abstract workflow tree description from file
    wfTreeManager = new DXManWorkflowTreeDesigner(blockchainIP);
    abstractWfTree = wfTreeManager.readWorkflowTreeDescription(abstractWfTreePath);    
    
    // The sum of tidiness must be 1
    operations[0] = new Operation("W_clothes", false, 500.0, 0.25);
    operations[1] = new Operation("W_dishes", false, 350.0, 0.25);
    operations[2] = new Operation("W_cook", true, 1300.0, 0.1);
    operations[3] = new Operation("W_robot", false, 150.0, 0.4);
  }
}
