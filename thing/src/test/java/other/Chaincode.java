package other;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javalite.http.Http;
import org.javalite.http.Multipart;
import org.javalite.http.Post;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Damian Arellanes
 */
public class Chaincode {
  
  public static void installChaincode(String networkId, String user, String password, 
    String chaincodeFile, String chaincodeId, String chaincodeVersion, String chaincodeType) {
    
    String uri = "https://blockchain-starter.eu-gb.bluemix.net/api/v1/networks/" + networkId + "/chaincode/install";
    
    Multipart mp = Http.multipart(uri)
      .basic(user, password)
      .header("Accept", "application/json")
      .header("Content-Type", "text/plain")
      .file("files", chaincodeFile)
      .field("chaincode_id", chaincodeId)
      .field("chaincode_version", chaincodeVersion)
      .field("chaincode_type", chaincodeType);    
    System.out.println("Response: " + mp.text());
  }
  
  public static void instantiateChaincode(String networkId, String user, String password, 
    String chaincodeId, String chaincodeVersion, String chaincodeType, List<String> chaincodeArguments) {
    
    /*JSONObject role1 = new JSONObject();
    role1.put("name", "admin");
    role1.put("mspId", "org1");
    JSONObject role2 = new JSONObject();
    role1.put("name", "admin");
    role1.put("mspId", "org2");
    JSONArray identities = new JSONArray();
    JSONObject r1 = new JSONObject(); r1.put("role", role1);
    JSONObject r2 = new JSONObject(); r2.put("role", role2);
    identities.put(r1); identities.put(r2);
    
    JSONArray signs = new JSONArray
    JSONObject policyaa = new JSONObject();
    policyaa.put("identities", identities);
    policyaa.put("policy", policy);*/
    
    String uri = "https://blockchain-starter.eu-gb.bluemix.net/api/v1/networks/" + networkId + "/channels/defaultchannel/chaincode/instantiate";
    
    String policy = "{" +
"    \"identities\": [" +
"      {" +
"        \"role\": {" +
"          \"name\": \"admin\"," +
"          \"mspId\": \"org1\"" +
"        }" +
"      }," +
"      {" +
"        \"role\": {" +
"          \"name\": \"admin\"," +
"          \"mspId\": \"org2\"" +
"        }" +
"      }" +
"    ]," +
"    \"policy\": {" +
"      \"1-of\": [" +
"        {" +
"          \"signed-by\": 0" +
"        }," +
"        {" +
"          \"signed-by\": 1" +
"        }" +
"      ]" +
"    }" +
"  }";
    
    try {
      
      JSONObject content = new JSONObject();
      content.put("chaincode_id", chaincodeId);
      content.put("chaincode_version", chaincodeVersion);
      content.put("chaincode_type", chaincodeType);
      content.put("chaincode_arguments", chaincodeArguments);
      content.put("endorsement_policy", new JSONObject(policy));
      
      System.out.println(content.toString());
      
      Post response = Http.post(uri, content.toString())
        .basic(user, password)
        .header("Content-Type", "application/json");
      
      System.out.println("Response: " + response.text());
      
    } catch (JSONException ex) { System.out.println(ex); }
  }
    
  public static void main(String[] args) {
    
    String networkId = "na602dc6a211548b9b4092561d45d5b12";
    String user = "org1";
    String password = "ViTSI_tXNCyaZOaluR0xPJfOcMCFdKDX7gRvijO_S4fYwHAo5uTXFgJ34QiUePUA";
    
    String chaincodeFile = "/home/darellanes/Downloads/blockchain/chaincodes/helloworld.go";
    String chaincodeId = "helloworld";
    String chaincodeVersion = "V1";
    String chaincodeType = "golang";
    
    //installChaincode(networkId, user, password, chaincodeFile, chaincodeId, chaincodeVersion, chaincodeType);
    //instantiateChaincode(networkId, user, password, chaincodeId, chaincodeVersion, chaincodeType, new ArrayList<>());
  }
}
