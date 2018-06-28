package com.dxman.dataspace.blockchain;

import com.dxman.dataspace.base.DXManDataSpace;
import java.util.List;
import org.javalite.http.Http;
import org.javalite.http.Post;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
  public void registerParameter(String parameterId, String value, 
    String writerId, List<String> readerIds) {
    
    try {
      
      JSONObject parameter = new JSONObject();
      parameter.put("$class", "com.dxman.blockchain.DXManParameter");
      parameter.put("parameterId", parameterId);
      parameter.put("value", value);
      parameter.put("writer", "com.dxman.blockchain.DXManNode#" + writerId);

      JSONArray receiversJSON = new JSONArray();
      for(String receiverId: readerIds) {
          receiversJSON.put("com.dxman.blockchain.DXManNode#" + receiverId);
      }
      parameter.put("readers", receiversJSON);

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
  public String readParameter(String parameterId, String readerId) {
    
    try {
      
      JSONObject query = new JSONObject();
      query.put("$class", "com.dxman.blockchain.DXManParameter");
      query.put("parameterId", parameterId);
      query.put("reader", "com.dxman.blockchain.DXManNode#readerId");

      StringBuilder url = new StringBuilder();
      url.append(blockChainEndpoint)
        .append("/api/queries/selectParameterByReader")
        .append("?parameterId=").append(parameterId).append("&")
        .append("reader=resource%3Acom.dxman.blockchain.DXManNode%23")
        .append(readerId);

      JSONArray result = new JSONArray(get(url.toString()));
      return ((JSONObject)result.get(0)).getString("value");
        
    } catch (JSONException ex) { System.err.println(ex.toString()); }

    return "";
  }

  @Override
  public void writeParameter(String parameterId, String newValue) {
    
    try {
      
      JSONObject transaction = new JSONObject();
      transaction.put("$class", "com.dxman.blockchain.UpdateParameter");
      transaction.put("parameter", "com.dxman.blockchain.DXManParameter#" + parameterId);
      transaction.put("newValue", newValue);

      Post result = post("http://localhost:3000/api/UpdateParameter", transaction.toString());
      
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
}
