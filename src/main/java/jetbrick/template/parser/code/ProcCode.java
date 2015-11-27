//========================================================================
//Copyright 2015 David Yu
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package jetbrick.template.parser.code;

import java.util.List;

/**
 * TODO
 * 
 * @author David Yu
 * @created Nov 23, 2015
 */
public class ProcCode extends MacroCode
{
    
    public String returnType;

    public ProcCode(ScopeCode scopeCode)
    {
        super(new MethodCode(scopeCode, "    ", false, true));
    }
    
    public boolean hasArgs()
    {
        return /*defineListCode != null && */defineListCode.size() != 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(512)
            .append("  // line: ").append(line).append('\n');
        
        List<SegmentCode> children = defineListCode.getChildren();
        
        if (returnType != null) {
            sb.append("  public static ")
                .append(returnType).append(' ').append(name).append('(')
                .append(children.get(0).toString());
            
            children = children.subList(1, children.size());
        } else {
            sb.append("  public static void ")
                .append(name).append("(final JetWriter $out");
        }

        if (children.size() > 0) {
            sb.append(',').append(' ').append(defineListCode.toString(children));
        }
        
        if (returnType != null)
            sb.append(") { // line: ");
        else
            sb.append(") throws Throwable { // line: ");
        
        sb.append(line).append('\n')
            .append(methodCode.toString());
        
        if (returnType != null)
            sb.append('\n');
        
        sb.append("  }\n");
        return sb.toString();
    }

}
