package test;

import com.dxman.execution.wttree.DXManWfOutputs;
import com.dxman.execution.wttree.DXManWorkflowTree;
import com.dxman.execution.wttree.DXManWfInputs;
import com.dxman.deployment.cli.DXManWorkflowTreeEditor;
import com.dxman.design.data.DXManDataChannelPoint;
import com.dxman.execution.common.DXManWfConditionOperator;

/**
 * @author Damian Arellanes
 */
public class WfTreeTest extends DXManWorkflowTreeEditor {
  
  private final String CUSTOMER = "d19ad318-0f00-4c73-b51d-9e8df0aa2752";
  private final String CUSTOMER_CREATE_NAME = "147f7882-d609-4799-9536-4c8c5bf386b7";
  private final String CUSTOMER_CREATE_ADDR = "15242ba9-1c37-426e-a1b1-364782540cc6";
  private final String CUSTOMER_CREATE_EMAIL = "7cd66110-7da0-4d75-85e2-9521c7fa7f34";
  private final String CUSTOMER_CREATE_ID = "d35e9d6c-59a2-4b22-a047-6301ddd2a3b7";
  private final String CUSTOMER_WELCEMAIL_EMAIL = "99d4b03d-07d8-4759-8169-34db7a828d65";
  private final String CUSTOMER_STD_NAME = "a953f643-7733-4647-8fe0-639af1bcc168";
  private final String CUSTOMER_STD_ADDR = "1203f042-7fc3-4c74-98d3-82f8d8935838";
  private final String CUSTOMER_STD_RES = "af8a642a-b89c-4704-b448-fe71fefea9d3";
  private final String CUSTOMER_FAST_NAME = "a5bbfce8-8fb9-4698-b66b-e07d4227e31b";
  private final String CUSTOMER_FAST_ADDR = "caa5d4f4-3542-4662-8c1c-09a67c8224cd";
  private final String CUSTOMER_FAST_RES = "39fcc077-4f88-4a8d-bdcc-4b37d70d924d";
  
  private final String LBP_CREATE = "af3e9823-69ff-4446-934e-5459781e62ba";
  
  private final String SENDER = "2907ff97-dc7c-4568-8afa-39de6f017ccd";
  
  private final String EMAIL_WELCEMAIL = "82a8b0bc-0181-46ff-95aa-bb37025a2d1d";
  
  private final String POST = "2055cafa-77a4-47af-b784-ffc61802ea38";
  /*private final String SEL1_ADDR = "106bd436-291b-4e13-8444-6b55c6d86590";
  private final String POST_STD_ADDR = "0de712a5-c7a2-4bc3-bdf4-080175e24fae";
  private final String POST_FAST_ADDR = "3ec2f520-5b1c-401f-a9f7-44dc0eded070";  */
  
  private final String COURIER1_STD = "a9fd6bbe-91ab-453e-83b4-ac009969fdd2";
  private final String COURIER2_FAST = "9108eb73-44c9-4b04-a42e-dddacdf478e1"; 
  

  public WfTreeTest(DXManWorkflowTree workflowTree, String wfId) {
    super(workflowTree, wfId);
  }

  @Override
  public void designControl() {

    // TODO the workflow tree can also include invocation nodes so the argument should be only the invocationNodeId
    
    // CustomerService, LPB.createRecord
    customiseOrder(CUSTOMER, LBP_CREATE, 0); 
    // CustomerService, SenderService
    customiseOrder(CUSTOMER, SENDER, 1);    
    
    // SenderService, PostService
    customiseOrder(SENDER, POST, 0);
    // SenderService, EmailService.sendWelcEmail
    customiseOrder(SENDER, EMAIL_WELCEMAIL, 1);
    
    /*// PostService, Courier1.sendWelcStd, SEL1.addr
    customiseSelector(POST, COURIER1_STD, 
      SEL1_ADDR, DXManWfConditionOperator.EQUAL, "Oxford St");
    // PostService, Courier2.sendWelcFast, SEL1.addr
    customiseSelector(POST, COURIER2_FAST, SEL1_ADDR, 
      DXManWfConditionOperator.EQUAL, "Oxford St");*/
    
    // PostService, Courier1.sendWelcStd
    customiseParallel(POST, COURIER1_STD, 1);
    // PostService, Courier2.sendWelcFast
    customiseParallel(POST, COURIER2_FAST, 1);
  }
  
  @Override
  public void designData() {
    
    /*// Post.sendWelcStd.addr --> SEL1.addr 
    DXManDataChannelPoint origin = new DXManDataChannelPoint(POST_STD_ADDR);
    DXManDataChannelPoint destination = new DXManDataChannelPoint(SEL1_ADDR);
    addDataChannel(POST, origin, destination);//Post,origin,dest
    
    // Post.sendWelcFast.addr --> SEL1.addr 
    DXManDataChannelPoint origin2 = new DXManDataChannelPoint(POST_FAST_ADDR);
    DXManDataChannelPoint destination2 = new DXManDataChannelPoint(SEL1_ADDR);
    addDataChannel(POST, origin2, destination2);//Post,origin,dest*/
  }

  @Override
  public DXManWfInputs getInputs() {
    DXManWfInputs wfInputs = new DXManWfInputs();
        
    wfInputs.put(CUSTOMER_CREATE_NAME, "Damian Arellanes"); // Customer.createRecord.name
    wfInputs.put(CUSTOMER_STD_NAME, "Damian Arellanes"); // Customer.sendWelcStd.name
    wfInputs.put(CUSTOMER_FAST_NAME, "Damian Arellanes"); // Customer.sendWelcFast.name
    
    wfInputs.put(CUSTOMER_CREATE_ADDR, "Oxford St"); // Customer.createRecord.addr
    wfInputs.put(CUSTOMER_STD_ADDR, "Oxford St"); // Customer.sendWelcStd.addr
    wfInputs.put(CUSTOMER_FAST_ADDR, "Oxford St"); // Customer.sendWelcFast.addr
    
    wfInputs.put(CUSTOMER_CREATE_EMAIL, "damian.arellanes@gmail.com"); // Customer.createRecord.email
    wfInputs.put(CUSTOMER_WELCEMAIL_EMAIL, "damian.arellanes@gmail.com"); // Customer.sendWelcEmail.email
    return wfInputs;
  }

  @Override
  public DXManWfOutputs getOutputs() {
    
    DXManWfOutputs wfOutputs = new DXManWfOutputs();    
    wfOutputs.add(CUSTOMER_STD_RES); // Customer.sendWelcStd.res
    wfOutputs.add(CUSTOMER_FAST_RES); // Customer.sendWelcFast.res
    wfOutputs.add(CUSTOMER_CREATE_ID); // Customer.createRecord.id
    //wfOutputs.add("Customer.sendWelcEmail.res");
    
    return wfOutputs;
  }
    
}
