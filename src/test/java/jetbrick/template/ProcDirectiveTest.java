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
    public void testValidBlockDirective()
    {
        TestUtil.assertEquals("1one", 
                "«test(1)»#test(int item)«item»#if(item == 1)one#endif#end", 
                engine);
    }
    
    @Test
    public void testAltIf()
    {
        TestUtil.assertEquals("1one", 
                "«test(1)»#test(int item)«item»«if(item == 1)»one«endif»#end", 
                engine);
    }
    
    @Test
    public void testAltIfElse()
    {
        TestUtil.assertEquals("1one", 
                "«test(1)»#test(int item)«item»«if(item == 0)»zero«else»one«endif»#end", 
                engine);
    }
    
    @Test
    public void testAltIfElseIf()
    {
        TestUtil.assertEquals("1one", 
                "«test(1)»#test(int item)«item»«if(item == 0)»zero«elseif(item == 1)»one«endif»#end", 
                engine);
    }
    
    @Test
    public void testAltFor()
    {
        TestUtil.assertEquals("1", 
                "«for(i : [1])»«i»«endfor»", 
                engine);
    }
    
    @Test
    public void testAltForElse()
    {
        TestUtil.assertEquals("one", 
                "«for(i : [])»«i»«else»one«endfor»", 
                engine);
    }
    
    @Test
    public void testValidStop()
    {
        TestUtil.assertEquals("1", 
                "«test(1)»#test(int item)«item»#stop(item == 1)one#end", 
                engine);
    }
    
    @Test
    public void testInvalidContextDirective()
    {
        TestUtil.assertFail(
                "«test(1)»#test(int item)«item»#if(item==1)one#put(\"2\", 2)#endif#end", 
                engine);
    }

}
