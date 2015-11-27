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

import jetbrick.template.JetEngine;
import jetbrick.template.resource.Resource;
import jetbrick.template.utils.PathUtils;
import jetbrick.template.utils.StringEscapeUtils;

/**
 * 模板 Class
 */
public class TemplateClassCode extends Code {
    
    public static final class Import {
        public final String name, path, fqcn;
        public Import(String name, String path, String fqcn) {
            this.name = name;
            this.path = path;
            this.fqcn = fqcn;
        }
    }
    
    private final String templateSuffix; // for imports
    private List<String[]> fields = new ArrayList<String[]>(32); // 全局文本字段
    private MethodCode methodCode = new MethodCode(null, "    ", false); // 方法体
    // TODO use this
    private LinkedHashMap<String,Import> imports = null;
    private List<MacroCode> macroCodeList; // 宏定义
    private List<ProcCode> procCodeList;
    private final JetEngine engine;
    private final Resource resource;
    
    public TemplateClassCode(JetEngine engine, Resource resource) {
        this.engine = engine;
        this.templateSuffix = engine.getConfig().getTemplateSuffix();
        this.resource = resource;
    }

    public void addField(String id, String text) {
        fields.add(new String[] { id, text });
    }
    
    public Import getImport(String name) {
        return imports == null ? null : imports.get(name);
    }
    
    public void addImport(String path) {
        int sl = path.lastIndexOf('/');
        // check relative
        if (sl == -1) {
            if ((sl = resource.name.lastIndexOf('/')) != -1) {
                path = resource.name.substring(0, sl + 1) + path;
            }
        } else if (sl == 1 && path.charAt(0) == '.') {
            if ((sl = resource.name.lastIndexOf('/')) != -1) {
                path = resource.name.substring(0, sl + 1) + path.substring(2);
            }
        }
        
        path = PathUtils.getStandardizedName(path);
        
        String fqcn;
        
        if (path.endsWith(templateSuffix)) {
            fqcn = Resource.resolveQualifiedClassName(path);
            path = path.substring(0, path.length() - templateSuffix.length());
        } else {
            fqcn = Resource.resolveQualifiedClassName(path + templateSuffix);
        }
        
        int slash = path.lastIndexOf('/');
        String name = slash == -1 ? path : path.substring(slash + 1);
        
        if (imports == null)
            imports = new LinkedHashMap<String, Import>();
        
        Import imp = new Import(name.replace('.', '_'), path, fqcn);
        imports.put(imp.name, imp);
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
        String packageName = resource.getPackageName(),
                className = resource.getClassName(),
                templateName = resource.getName(),
                encoding = resource.getEncoding();
        
        StringBuilder sb = new StringBuilder(2048);
        if (packageName != null) {
            sb.append("package " + packageName + ";\n");
            sb.append("\n");
        }
        sb.append("import java.util.*;\n");
        sb.append("import jetbrick.template.JetContext;\n");
        sb.append("import jetbrick.template.runtime.*;\n");
        
        if (imports != null) {
            for (Import i : imports.values()) {
                engine.getTemplate(i.path + templateSuffix);
                sb.append("import ")
                    .append(i.fqcn)
                    .append(";\n");
            }
        }
        
        sb.append("\n");
        sb.append("@SuppressWarnings({\"all\", \"warnings\", \"unchecked\", \"unused\", \"cast\"})\n");
        sb.append("public final class " + className + " extends JetPage {\n\n");
        
        if (imports != null) {
            sb.append("  static final LinkedHashMap<String,String> imports = new LinkedHashMap<String,String>();\n");
            sb.append("  static {\n");
            for (Import i : imports.values()) {
                sb.append("    imports.put(\"").append(i.name).append("\", \"")
                    .append(i.path).append("\");\n");
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
