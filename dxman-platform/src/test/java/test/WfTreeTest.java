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
  
  private final String CUSTOMER = "86a2f163-29ae-4797-9076-ca4301ad26f9";
  private final String CUSTOMER_CREATE_NAME = "2ef8490d-e0df-4899-ae75-24aeb7f05172";
  private final String CUSTOMER_CREATE_ADDR = "8540d8c3-8751-4fc1-8713-dbe1240c68c6";
  private final String CUSTOMER_CREATE_EMAIL = "90f3435a-7a8c-4478-8e36-9282d8bf4736";
  private final String CUSTOMER_CREATE_ID = "ebf86630-064d-4f7e-bf0a-0da0125845e3";
  private final String CUSTOMER_WELCEMAIL_EMAIL = "727d5175-c292-4a4a-a4d2-82c393a2a42d";
  private final String CUSTOMER_STD_NAME = "ee6d09ca-c107-4ea6-b373-72d301bc19ba";
  private final String CUSTOMER_STD_ADDR = "a174b21e-7dbc-472a-b7d4-b7cfa4245751";
  private final String CUSTOMER_STD_RES = "0989988e-8889-48b6-b4ac-3417fa524888";
  private final String CUSTOMER_FAST_NAME = "641ae472-c75c-4126-aba1-09803648a2ca";
  private final String CUSTOMER_FAST_ADDR = "bae8377c-bf6f-44eb-b25f-231823ddd1d4";
  private final String CUSTOMER_FAST_RES = "7c48e545-0b06-45ab-9a59-72937956e53d";
  
  private final String LBP_CREATE = "de757085-64e1-4919-b9e0-4e706450f344";
  
  private final String SENDER = "46cd5b5b-5c55-490f-bfd3-a5827834b52e";
  
  private final String EMAIL_WELCEMAIL = "716f900b-3979-4a45-9b1f-3c48b360e7ba";
  
  private final String POST = "7424b5ab-13ec-4210-bde5-e786d98c5e06";
  private final String SEL1_ADDR = "106bd436-291b-4e13-8444-6b55c6d86590";
  private final String POST_STD_ADDR = "fa4fbe78-8563-4bae-88f5-d88f4032ebcd";
  private final String POST_FAST_ADDR = "3ec2f520-5b1c-401f-a9f7-44dc0eded070";
  
  private final String COURIER1_STD = "a87976b3-9db3-4835-9c73-b69627470fac";
  private final String COURIER2_FAST = "7c61e716-8b80-4128-942f-2fd04dde71bb"; 
  
  private final String GUA = "2b63d84b-b629-47ee-87b4-bf6f5c072ed7";
  private final String GUA_INPUT = "3f078876-e355-4f3b-b20e-d806c8c40791";
  

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
    //customiseParallel(POST, COURIER1_STD, 1);
    customiseParallel(POST, GUA, 1);
    // PostService, Courier2.sendWelcFast
    customiseParallel(POST, COURIER2_FAST, 1);
    
    // GUA1, Courier1.sendWelcStd
    customiseGuard(GUA, COURIER1_STD, GUA_INPUT, DXManWfConditionOperator.EQUAL, "Oxford St");
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
    
    // Post.sendWelcFast.addr --> SEL1.addr 
    DXManDataChannelPoint origin3 = new DXManDataChannelPoint(POST_STD_ADDR);
    DXManDataChannelPoint destination3 = new DXManDataChannelPoint(GUA_INPUT);
    addDataChannel(POST, origin3, destination3);//Post,origin,dest
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
