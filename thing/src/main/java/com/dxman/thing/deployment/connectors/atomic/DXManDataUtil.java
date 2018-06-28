package com.dxman.thing.deployment.connectors.atomic;

import com.dxman.design.data.DXManOperation;
import com.dxman.design.data.DXManParameter;
import com.dxman.utils.DXManConfiguration;
import com.dxman.utils.DXManMap;
import java.util.regex.Pattern;

/**
 * @author Damian Arellanes
 */
public class DXManDataUtil {
  
  public class Precompiled {
    
    private final Pattern pattern;
    private final String[] outputIds;

    public Precompiled(Pattern pattern, String[] outputIds) {
      this.pattern = pattern;
      this.outputIds = outputIds;
    }

    public Pattern getPattern() { return pattern; } 
    public String[] getOutputIds() { return outputIds; }
  }
  
  private final DXManMap<String, Precompiled> operationPatterns;

  public DXManDataUtil(DXManMap<String, DXManOperation> operations) {
    this.operationPatterns = new DXManMap<>();
    preCompilePatterns(operations);
  }
  
  private void preCompilePatterns(DXManMap<String, DXManOperation> operations) {
        
    operations.forEach((opKey,op)->{
      
      // Escape taken from https://stackoverflow.com/questions/14134558/
      //list-of-all-special-characters-that-need-to-be-escaped-in-a-regex/
      //27454382#27454382
      String  basePattern = op.getBindingInfo().getResponseTemplate().replaceAll(
        "[\\<\\(\\[\\{\\\\\\^\\-\\=\\$\\!\\|\\]\\}\\)\\?\\*\\+\\.\\>]", "\\\\$0"
      );
      
      // TODO Parameters should be lists not maps
      int i = 0;
      String[] outputIds = new String[op.getOutputs().size()];      
      for(DXManParameter output: op.getOutputs().values()) {
      
        basePattern = basePattern.replace(
          DXManConfiguration.WILDCARD_START + output.getName() + 
            DXManConfiguration.WILDCARD_END, "(.*)"
        );
        outputIds[i++] = output.getId();
      }
      
      getOperationPatterns().put(opKey, new Precompiled(
        Pattern.compile(basePattern), outputIds)
      );
    });
  }

  public DXManMap<String, Precompiled> getOperationPatterns() {
    return operationPatterns;
  }
}
