package com.dxman.dataspace.blockchain;

import com.dxman.dataspace.base.DXManDataSpace;
import static com.dxman.utils.DXManIDGenerator.generateParameterUUID;
import java.util.*;
import org.javalite.http.*;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class DXManBlockChainManager implements DXManDataSpace {
  
  private final String blockChainEndpoint;
  
  public DXManBlockChainManager(String blockChainEndpoint) {
    this.blockChainEndpoint = blockChainEndpoint;
  }
  
  @Override
  public int registerThing(String id, String alias) {
    
    try {
      
      JSONObject node = new JSONObject();
      node.put("$class", "com.dxman.blockchain.DXManNode");
      node.put("nodeId", id);
      node.put("alias", alias);

      Post result = post(
        blockChainEndpoint + "/api/DXManNode", 
        node.toString()
      );

      switch(result.responseCode()) {
        case 500:
          System.err.println("Participant " + id +" already exists!");
          break;
        case 200:
          System.out.println("Participant " + id +" has been added!");
          break;
      }

      return result.responseCode();
    } catch (JSONException ex) { System.err.println(ex.toString()); }
    
    return 200;
  }
  
  @Override
  public void registerParameter(String parameterId, String workflowId, 
    String value, List<String> readers) {
    
    try {
      
      JSONObject parameter = new JSONObject();
      parameter.put("$class", "com.dxman.blockchain.DXManParameter");
      parameter.put("id", generateParameterUUID(parameterId, workflowId));
      parameter.put("parameterId", parameterId);
      parameter.put("workflowId", workflowId);
      parameter.put("value", value);
      JSONArray readersJSON = new JSONArray();
      for(String readerId: readers) {
                
        readersJSON.put("com.dxman.blockchain.DXManParameter#" 
          + generateParameterUUID(readerId, workflowId));
      }
      parameter.put("readers", readersJSON);

      Post result = post(
        blockChainEndpoint + "/api/DXManParameter", 
        parameter.toString()
      );
      
      if(result.responseCode() == 200) {
        System.out.println("Parameter " + parameterId + " added to the blockchain");
      } else {
        System.err.println(result.responseMessage());
      }
        
    } catch (JSONException ex) {
        System.err.println(ex.toString());
    }
  }
  
  @Override
  public String readParameter(String parameterId, String workflowId) {
    
    try {
      
      JSONObject query = new JSONObject();
      query.put("$class", "com.dxman.blockchain.DXManParameter");
      query.put("parameterId", parameterId);
      query.put("workflowId", workflowId);

      StringBuilder url = new StringBuilder();
      url.append(blockChainEndpoint)
        .append("/api/queries/selectWorkflowParameter")
        .append("?parameterId=").append(parameterId).append("&")
        .append("workflowId=").append(workflowId);

      JSONArray result = new JSONArray(get(url.toString()));
      return ((JSONObject)result.get(0)).getString("value");
        
    } catch (JSONException ex) { System.err.println(ex.toString()); }

    return "";
  }
  
  @Override
  public void writeParameter(String parameterId, String workflowId, 
    String newValue) {
    
    try {
      
      JSONObject transaction = new JSONObject();
      transaction.put("$class", "com.dxman.blockchain.UpdateParameter");
      transaction.put("parameter", "com.dxman.blockchain.DXManParameter#" + 
        generateParameterUUID(parameterId, workflowId));
      transaction.put("newValue", newValue);

      Post result = post(blockChainEndpoint + 
        "/api/UpdateParameter", transaction.toString());
      
      if(result.responseCode() == 200) {
        System.out.println("Parameter " + parameterId + " has been updated!");
      }
        
    } catch (JSONException ex) { System.err.println(ex.toString()); }
  }
  
  private Post post(String url, String content) {
    return Http.post(url, content)
      .header("Accept", "application/json")
      .header("Content-Type", "application/json");
  }
    
  private String get(String url) {
    return Http.get(url).text();
  }
  
  public static void main(String[] args) {
    
    DXManBlockChainManager blockchain = new DXManBlockChainManager("http://localhost:3000");
    
    String par1UUID = generateParameterUUID("Par1", "MyWfId");
    String par2UUID = generateParameterUUID("Par2", "MyWfId");
    String par3UUID = generateParameterUUID("Par3", "MyWfId");
    
    List<String> readers = Arrays.asList(par2UUID, par3UUID);
    blockchain.registerParameter("Par1", "MyWfId", "Hello World Initial", readers);
    List<String> readers2 = new ArrayList<>();//Arrays.asList("Par100");
    blockchain.registerParameter("Par2", "MyWfId", "null", readers2);
    List<String> readers3 = new ArrayList<>(); //Arrays.asList("Par20000");
    blockchain.registerParameter("Par3", "MyWfId", "null", readers3);
    
    System.out.println("Par1-->" + blockchain.readParameter("Par1", "MyWfId"));
    System.out.println("Par2-->" + blockchain.readParameter("Par2", "MyWfId"));
    System.out.println("Par3-->" + blockchain.readParameter("Par3", "MyWfId"));
    
    System.out.println("----------------UPDATING...----------");
    blockchain.writeParameter("Par1", "MyWfId", "ANOTHER UPDATE HAS BEEN DONE!!!");
    
    System.out.println("Par1-->" + blockchain.readParameter("Par1", "MyWfId"));
    System.out.println("Par2-->" + blockchain.readParameter("Par2", "MyWfId"));
    System.out.println("Par3-->" + blockchain.readParameter("Par3", "MyWfId"));
  }
}
