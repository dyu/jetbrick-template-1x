/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2014 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.parser.code;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jetbrick.template.resource.Resource;
import jetbrick.template.utils.PathUtils;
import jetbrick.template.utils.StringEscapeUtils;

/**
 * 模板 Class
 */
public class TemplateClassCode extends Code {
    private String packageName; // 生成的包名
    private String className; // 生成的类名
    private String templateName; // 模板名称
    private String templateSuffix; // for imports
    private String encoding; // 模板默认输出编码
    private List<String[]> fields = new ArrayList<String[]>(32); // 全局文本字段
    private MethodCode methodCode = new MethodCode(null, "    ", false); // 方法体
    // TODO use this
    private LinkedHashMap<String,String> imports = null;
    private List<MacroCode> macroCodeList; // 宏定义
    private List<ProcCode> procCodeList;
    
    public TemplateClassCode(String templateSuffix) {
        this.templateSuffix = templateSuffix;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void addField(String id, String text) {
        fields.add(new String[] { id, text });
    }
    
    public String getImportedPath(String name) {
        return imports == null ? null : imports.get(name);
    }
    
    public void addImport(String path) {
        if (path.endsWith(templateSuffix)) {
            path = path.substring(0, path.length() - templateSuffix.length());
        }
        
        path = PathUtils.getStandardizedName(path);
        
        int slash = path.lastIndexOf('/');
        String name = slash == -1 ? path : path.substring(slash + 1);
        
        if (imports == null)
            imports = new LinkedHashMap<String, String>();
        
        imports.put(name, path);
    }

    public void addMacro(MacroCode macroCode) {
        if (macroCodeList == null) {
            macroCodeList = new ArrayList<MacroCode>(8);
        }
        macroCodeList.add(macroCode);
    }
    
    public void addProc(ProcCode procCode) {
        if (procCodeList == null) {
            procCodeList = new ArrayList<ProcCode>(8);
        }
        procCodeList.add(procCode);
    }

    public MethodCode getMethodCode() {
        return methodCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(2048);
        if (packageName != null) {
            sb.append("package " + packageName + ";\n");
            sb.append("\n");
        }
        sb.append("import java.util.*;\n");
        sb.append("import jetbrick.template.JetContext;\n");
        sb.append("import jetbrick.template.runtime.*;\n");
        
        if (imports != null) {
            for (String path : imports.values()) {
                sb.append("import ")
                    .append(Resource.resolveQualifiedClassName(path))
                    .append(";\n");
            }
        }
        
        sb.append("\n");
        sb.append("@SuppressWarnings({\"all\", \"warnings\", \"unchecked\", \"unused\", \"cast\"})\n");
        sb.append("public final class " + className + " extends JetPage {\n\n");
        
        if (imports != null) {
            sb.append("  static final LinkedHashMap<String,String> imports = new LinkedHashMap<String,String>();\n");
            sb.append("  static {\n");
            for (Map.Entry<String, String> entry : imports.entrySet()) {
                sb.append("    imports.put(\"").append(entry.getKey()).append("\", \"")
                    .append(entry.getValue()).append("\");\n");
            }
            sb.append("  }\n\n");
            
            sb.append("  @Override\n");
            sb.append("  public LinkedHashMap<String,String> getImports() {\n")
                .append("    return imports;\n")
                .append("  }\n\n");
        }
        
        sb.append("  @Override\n");
        sb.append("  public void render(final JetPageContext $ctx) throws Throwable {\n");
        sb.append(methodCode.toString());
        sb.append("  }\n");
        sb.append("\n");
        if (macroCodeList != null) {
            for (MacroCode c : macroCodeList) {
                sb.append(c.toString()).append('\n');
            }
        }
        if (procCodeList != null) {
            for (ProcCode c : procCodeList) {
                sb.append(c.toString()).append('\n');
            }
        }
        sb.append("  @Override\n");
        sb.append("  public String getName() {\n");
        sb.append("    return \"" + StringEscapeUtils.escapeJava(templateName) + "\";\n");
        sb.append("  }\n");
        sb.append("\n");
        sb.append("  public static final String $ENC = \"" + encoding + "\";\n");
        for (String[] field : fields) {
            sb.append("  private static final String " + field[0] + " = \"" + StringEscapeUtils.escapeJava(field[1]) + "\";\n");
            sb.append("  private static final byte[] " + field[0] + "_bytes = JetUtils.asBytes(" + field[0] + ", $ENC);\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

}
