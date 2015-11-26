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

package jetbrick.template;

import java.util.LinkedHashMap;
import java.util.Properties;

import jetbrick.template.resource.loader.FileSystemResourceLoader;
import jetbrick.template.runtime.JetPage;

import org.junit.Assert;
import org.junit.Test;

/**
 * TODO
 * 
 * @author David Yu
 * @created Nov 26, 2015
 */
public class ImportDirectiveTest
{
    
    private static JetEngine createEngine(String basePath) {
        Properties config = new Properties();
        //config.put(JetConfig.IMPORT_PACKAGES, "testcase.model.*");
        //config.put(JetConfig.IMPORT_VARIABLES, "User user, Book book");
        //config.put(JetConfig.IMPORT_TAGS, TagUtils.class.getName());
        //config.put(JetConfig.OUTPUT_ENCODING, "GBK");
        config.put(JetConfig.TEMPLATE_LOADER, FileSystemResourceLoader.class.getName());
        config.put(JetConfig.TEMPLATE_PATH, "src/test/resources/");
        config.put(JetConfig.TEMPLATE_SUFFIX, ".jetx");
        //config.put(JetConfig.COMPILE_ALWAYS, "false");
        //config.put(JetConfig.COMPILE_TOOL, JdtCompiler.class.getName());
        //config.put(JetConfig.COMPILE_STRATEGY, "always");
        config.put(JetConfig.COMPILE_DEBUG, "true");
        config.put(JetConfig.TRIM_DIRECTIVE_COMMENTS, "true");
        return JetEngine.create(config);
    }
    
    static void verify(String basePath, String importPath, String expect, 
            String name)
    {
        JetEngine engine = createEngine(basePath);
        JetTemplate template = engine.createTemplate(
                "#import " + importPath + "\n");
        
        JetPage page = template.getJetPage();
        LinkedHashMap<String, String> imports = page.getImports();
        Assert.assertTrue(imports != null && imports.size() == 1);
        Assert.assertEquals(expect, imports.get(name));
    }
    
    @Test
    public void testPlaceholder()
    {
        
    }
    
    // TODO compile the imported templates first before compiling the template itself.
    /*@Test
    public void testWithSuffix()
    {
        verify("src/test/resources/", "template/proc.jetx", "/template/proc", "proc");
    }
    
    @Test
    public void testWithoutSuffix()
    {
        verify("src/test/resources/", "template/proc", "/template/proc", "proc");
    }
    
    @Test
    public void testAbsoluteWithSuffix()
    {
        verify("src/test/resources/", "/template/proc.jetx", "/template/proc", "proc");
    }
    
    @Test
    public void testAbsoluteWithoutSuffix()
    {
        verify("src/test/resources/", "/template/proc", "/template/proc", "proc");
    }
    
    @Test
    public void testRelativeWithSuffix()
    {
        verify("src/test/resources/template/", "proc.jetx", "/proc", "proc");
    }
    
    @Test
    public void testRelativeWithoutSuffix()
    {
        verify("src/test/resources/template/", "proc", "/proc", "proc");
    }
    
    @Test
    public void testDotRelativeWithSuffix()
    {
        verify("src/test/resources/template/", "./proc.jetx", "/proc", "proc");
    }
    
    @Test
    public void testDotRelativeWithoutSuffix()
    {
        verify("src/test/resources/template/", "./proc", "/proc", "proc");
    }
    
    @Test
    public void testCallImported()
    {
        JetEngine engine = createEngine("src/test/resources/");
        JetTemplate template = engine.createTemplate(
                "\n#import /template/proc\n«proc::test(\"foo\",\"bar\", 1)»");
        
        JetPage page = template.getJetPage();
        LinkedHashMap<String, String> imports = page.getImports();
        Assert.assertTrue(imports != null && imports.size() == 1);
        Assert.assertEquals("/template/proc", imports.get("proc"));
    }*/

}
