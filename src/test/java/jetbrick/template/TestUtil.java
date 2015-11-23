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

import jetbrick.template.parser.SyntaxErrorException;
import jetbrick.template.utils.UnsafeCharArrayWriter;

import org.junit.Assert;

/**
 * TODO
 * 
 * @author David Yu
 * @created Nov 23, 2015
 */
public final class TestUtil
{
    private TestUtil() {}
    
    static void assertEquals(String expected, String template, 
            JetEngine engine)
    {
        JetTemplate jt = engine.createTemplate(template);
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        JetContext context = new JetContext();
        jt.render(context, out);
        Assert.assertEquals(expected, out.toString());
    }
    
    static void assertFail(String template, JetEngine engine)
    {
        try
        {
            engine.createTemplate(template);
            Assert.fail();
        }
        catch (SyntaxErrorException e)
        {
            // expected
        }
    }

}
