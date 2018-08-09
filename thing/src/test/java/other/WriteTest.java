package other;



import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Damian Arellanes
 */
public class WriteTest {
 
  public static Map<String , String> getValues(String template, String concrete) {
    

    Map<String, String> parameters = new HashMap<>();
    parameters.put("return_value", "return_value");
    parameters.put("id", "id");
    parameters.put("name", "name");
    parameters.put("hardware", "hardware");
    parameters.put("connected", "connected");
    
    /* BEGIN: THIS CODE SHOULD BE DONE ON DEPLOYMENT */
    // Escape taken from https://stackoverflow.com/questions/14134558/list-of-all-special-characters-that-need-to-be-escaped-in-a-regex/27454382#27454382
    String  basePattern = template.replaceAll(
      "[\\<\\(\\[\\{\\\\\\^\\-\\=\\$\\!\\|\\]\\}\\)\\?\\*\\+\\.\\>]", "\\\\$0"
    );
    
    // Parameters should be lists not maps
    String[] ids = new String[parameters.size()];
    int i = 0;
    for(String parameter: parameters.values()) {
      basePattern = basePattern.replace("##"+ parameter +"##", "(.*)");
      ids[i++] = parameter;
    }
    /* END: THIS CODE SHOULD BE DONE ON DEPLOYMENT */
    
    Map<String , String> parameterValues = new HashMap<String , String>();
    Pattern pattern = Pattern.compile(basePattern);
    Matcher matcher = pattern.matcher(concrete);
    System.out.println(concrete.length());
    if(matcher.matches()) {
      
      for(i = 1; i <= matcher.groupCount(); i++) {
        System.out.println(ids[i-1] + "-->" + matcher.group(i));
      }
    } else {
      System.out.println("The JSON document does not match the template!");
    }
    
    return parameterValues;
  }
  
  public static void main(String[] args) {
    
    String resTemp = "{\"return_value\": ##return_value##, \"id\": \"##id##\", \"name\": \"##name##\", \"hardware\": \"##hardware##\", \"connected\": ##connected##}";
    String resReal = "{\"return_value\": 1, \"id\": \"1\", \"name\": \"esp8266_IoT_Node\", \"hardware\": \"esp8266\", \"connected\": true}";
    
    getValues(resTemp, resReal);
  }
}
