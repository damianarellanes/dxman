package test;

import com.dxman.deployment.cli.DXManWorkflowTreeEditor;
import com.dxman.design.data.DXManDataChannelPoint;
import com.dxman.execution.*;
import com.dxman.execution.selector.DXManWfConditionOperator;

/**
 * @author Damian Arellanes
 */
public class WfTreeTest extends DXManWorkflowTreeEditor {

  public WfTreeTest(DXManWorkflowTree workflowTree, String wfId) {
    super(workflowTree, wfId);
  }

  @Override
  public void designControl() {

    // TODO the workflow tree can also include invocation nodes so the argument should be only the invocationNodeId
    
    /*// CustomerService, LPB.createRecord
    customiseOrder("32dbf901-d80c-4855-8407-5b638e177975", "25fb08a3-a471-4492-a99d-2fc9f36c79ef", 0); 
    // CustomerService, SenderService
    customiseOrder("32dbf901-d80c-4855-8407-5b638e177975", "c26cf571-636f-47d6-9734-a50857001d30", 1);    
    
    // SenderService, PostService
    customiseOrder("c26cf571-636f-47d6-9734-a50857001d30", "5798c6d1-30cf-44cc-b69e-6fd92924aa3f", 0);
    // SenderService, EmailService.sendWelcEmail
    customiseOrder("c26cf571-636f-47d6-9734-a50857001d30", "064eba5e-ec08-490d-8298-cbf0c51ba3d6", 1);*/
    
    // PostService, Courier1.sendWelcStd, SEL1.addr
    customiseSelector("26c44521-6966-49b1-b5be-6a87d237b28e", "3becbdf1-0f35-4d58-9812-80dcb3307553", 
      "7605fb00-a57c-4cfb-bf83-da51bf031ad7", DXManWfConditionOperator.EQUAL, "Oxford St");
    // PostService, Courier2.sendWelcFast, SEL1.addr
    customiseSelector("26c44521-6966-49b1-b5be-6a87d237b28e", "3c271298-1d6d-41fe-947d-450b52654529", 
      "7605fb00-a57c-4cfb-bf83-da51bf031ad7", DXManWfConditionOperator.EQUAL, "Oxford St");
  }
  
  @Override
  public void designData() {
    
    // Post.sendWelcStd.addr --> SEL1.addr 
    DXManDataChannelPoint origin = new DXManDataChannelPoint(
      "acfbc24a-de0b-4129-8e5b-b8b435640355"
    );
    DXManDataChannelPoint destination = new DXManDataChannelPoint(
      "7605fb00-a57c-4cfb-bf83-da51bf031ad7"
    );
    addDataChannel("26c44521-6966-49b1-b5be-6a87d237b28e", origin, destination);//Post,origin,dest
    
    // Post.sendWelcFast.addr --> SEL1.addr 
    DXManDataChannelPoint origin2 = new DXManDataChannelPoint(
      "6a6cb221-a5d6-4581-b18f-874020a62d94"
    );
    DXManDataChannelPoint destination2 = new DXManDataChannelPoint(
      "7605fb00-a57c-4cfb-bf83-da51bf031ad7"
    );
    addDataChannel("26c44521-6966-49b1-b5be-6a87d237b28e", origin2, destination2);//Post,origin,dest
  }

  @Override
  public DXManWfInputs getInputs() {
    DXManWfInputs wfInputs = new DXManWfInputs();
        
//    wfInputs.put("a5caca64-98ef-411d-9ce8-e0010313ede8", "Damian Arellanes"); // Customer.createRecord.name
//    wfInputs.put("0e13975a-6ea7-46da-af65-409ac51f8176", "Damian Arellanes"); // Customer.sendWelcStd.name
//    wfInputs.put("e4f487d4-47a4-40e8-b754-3a1c500ba04b", "Damian Arellanes"); // Customer.sendWelcFast.name
    
    wfInputs.put("4f24e81f-83a5-4f60-b40e-2d306487b5ce", "Oxford St 2"); // Customer.createRecord.addr
    wfInputs.put("626773ea-b339-4a91-a2d9-e6043b831b68", "Oxford St 2"); // Customer.sendWelcStd.addr
    //wfInputs.put("3e836a9f-3bf3-4383-8168-50362c82056f", "Oxford St"); // Customer.sendWelcFast.addr
    
//    wfInputs.put("cba1e6d0-251b-4628-9150-f167b58d557e", "damian.arellanes@gmail.com"); // Customer.createRecord.email
//    wfInputs.put("e2bdc9fc-6ac8-4672-a3fc-9a025cf26e29", "damian.arellanes@gmail.com"); // Customer.sendWelcEmail.email
    return wfInputs;
  }

  @Override
  public DXManWfOutputs getOutputs() {
    
    DXManWfOutputs wfOutputs = new DXManWfOutputs();    
    wfOutputs.add("997309a8-fb2a-4859-a554-624983854b8f"); // Customer.sendWelcStd.res
    wfOutputs.add("ef404cde-419c-4f25-90f9-c2a88a77dc39"); // Customer.sendWelcFast.res
    wfOutputs.add("61ee32df-d117-411d-8d79-94ed73aaa45b"); // Customer.createRecord.id
    //wfOutputs.add("Customer.sendWelcEmail.res");
    
    return wfOutputs;
  }
    
}
