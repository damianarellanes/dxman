package com.dxman.autonomicsamples.smarthome;

import com.dxman.deployment.cli.DXManWorkflowTreeEditor;

/**
 * @author Damian Arellanes
 */
public class SmarthomeEffector {

  public SmarthomeEffector() {}
  
  public void executeConcreteWfTree(DXManWorkflowTreeEditor wfEditor) {
    
    // Customizes the concrete workflow tree 
    SmarthomeKnowledge.wfTreeManager.customizeConcreteWfTree(wfEditor, false);
    
    // Executes the concrete workflow tree
    String topService = SmarthomeKnowledge.abstractWfTree.getCompositeService().getId();
    SmarthomeKnowledge.wfTreeManager.executeWorkflow(
      wfEditor, SmarthomeKnowledge.abstractWfTree.getWt().get(topService)
    );
  }
}
