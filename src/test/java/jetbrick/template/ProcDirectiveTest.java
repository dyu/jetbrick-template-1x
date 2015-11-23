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

import jetbrick.template.ForDirectiveTest.MyEnum;
import jetbrick.template.parser.SyntaxErrorException;
import jetbrick.template.utils.UnsafeCharArrayWriter;

import org.junit.Assert;
import org.junit.Test;

/**
 * TODO
 * 
 * @author David Yu
 * @created Nov 23, 2015
 */
public class ProcDirectiveTest
{
    private JetEngine engine = JetEngine.create();
    
    @Test
    public void testValid()
    {
        JetTemplate template = engine.createTemplate("«test(1)»#test(int item)«item»#if(item==1)one#endif#end");
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        JetContext context = new JetContext();
        context.put("items", MyEnum.class);
        template.render(context, out);
        Assert.assertEquals("1one", out.toString());
    }
    
    @Test
    public void testInvalid()
    {
        try
        {
            engine.createTemplate("«test(1)»#test(int item)«item»#if(item==1)one#put(\"2\", 2)#endif#end");
            Assert.fail();
        }
        catch (SyntaxErrorException e)
        {
            // expected
            e.printStackTrace();
        }
    }

}
