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
    public void testDelimClose()
    {
        TestUtil.assertEquals("»true»»«»»", 
                "»#if(true)true»#endif»«»»", 
                engine);
    }
    
    @Test
    public void testDelimCloseAndEmpy()
    {
        TestUtil.assertEquals("»true»true»«»«»»«»«»", 
                "»#if(true)true»#endif#if(true)true»«»«»#endif»«»«»", 
                engine);
    }
    
    @Test
    public void testDelimEmpty()
    {
        TestUtil.assertEquals("«»«»«»«»«»«»", 
                "«»«»#if(true)«»«»#endif«»«»", 
                engine);
    }
    
    @Test
    public void testDelim2x()
    {
        TestUtil.assertEquals("««", 
                "««", 
                engine);
    }
    
    @Test
    public void testDelimLast()
    {
        TestUtil.assertEquals("»«»true«", 
                "»«»«true»«", 
                engine);
    }
    
    @Test
    public void testDelimLast2x()
    {
        TestUtil.assertEquals("»«»true««", 
                "»«»«true»««", 
                engine);
    }
    
    // Currently fails
    //@Test
    public void testDelimInsideIf()
    {
        TestUtil.assertEquals("»«»1one«", 
                "»«»«test(1)»#test(int item)«item»#if(item == 1)one«#endif#end", 
                engine);
    }
    
    // Currently fails
    //@Test
    public void testDelimInsideAltIf()
    {
        TestUtil.assertEquals("»«»1one«", 
                "»«»«test(1)»#test(int item)«item»«if(item == 1)»one««endif»#end", 
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
    public void testAltForOutsideProc()
    {
        TestUtil.assertEquals("1020", 
                "«for(int i : [1,2])»«i»«test(0)»«endfor»#test(int item)«item»#end", 
                engine);
    }
    
    @Test
    public void testAltForElseOutSideProc()
    {
        TestUtil.assertEquals("one0", 
                "«for(i : [])»«i»«else»one«test(0)»«endfor»#test(int item)«item»#end", 
                engine);
    }
    
    @Test
    public void testAltFor()
    {
        TestUtil.assertEquals("123", 
                "«test([1, 2, 3])»#test(List<Integer> items)«for(item : items)»«item»«endfor»#end", 
                engine);
    }
    
    @Test
    public void testAltForElse()
    {
        TestUtil.assertEquals("none", 
                "«test([])»#test(List<Integer> items)«for(item : items)»«item»«else»none«endfor»#end", 
                engine);
    }
    
    @Test
    public void testAltForTyped()
    {
        TestUtil.assertEquals("123", 
                "«test([1, 2, 3])»#test(List<Integer> items)«for(Integer item : items)»«item»«endfor»#end", 
                engine);
    }
    
    @Test
    public void testAltForElseTyped()
    {
        TestUtil.assertEquals("none", 
                "«test([])»#test(List<Integer> items)«for(Integer item : items)»«item»«else»none«endfor»#end", 
                engine);
    }
    
    @Test
    public void testAltForTypedPrimitive()
    {
        TestUtil.assertEquals("123", 
                "«test([1, 2, 3])»#test(List<Integer> items)«for(int item : items)»«item»«endfor»#end", 
                engine);
    }
    
    @Test
    public void testAltForElseTypedPrimitive()
    {
        TestUtil.assertEquals("none", 
                "«test([])»#test(List<Integer> items)«for(int item : items)»«item»«else»none«endfor»#end", 
                engine);
    }
    
    // TODO
    //@Test
    public void testAltForTypedPrimitiveArg()
    {
        TestUtil.assertEquals("123", 
                "«test([1, 2, 3])»#test(int[] items)«for(int item : items)»«item»«endfor»#end", 
                engine);
    }
    
    // TODO
    //@Test
    public void testAltForElseTypedPrimitiveArg()
    {
        TestUtil.assertEquals("none", 
                "«test([])»#test(int[] items)«for(int item : items)»«item»«else»none«endfor»#end", 
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
    
    @Test
    public void testFunction()
    {
        TestUtil.assertEquals("2one", 
                "«test(incr(1))»#test(int item)«item»#stop(item == 1)one#end#incr(int x): int\nreturn x + 1;\n#end", 
                engine);
    }
    
    @Test
    public void testEmit()
    {
        TestUtil.assertEquals("3one", 
                "«test(incr(1))»#test(int item)«#emit»\nitem++;\n«#»«item»#stop(item == 1)one#end#incr(int x): int\nreturn x + 1;\n#end", 
                engine);
    }
    
    @Test
    public void testTrailingSpaceAfterNewLine()
    {
        TestUtil.assertEquals("3one\n ", 
                "«test(incr(1))»\n #test(int item)«#emit»\nitem++;\n«#»«item»#stop(item == 1)one#end#incr(int x): int\nreturn x + 1;\n#end", 
                engine);
    }
    
    @Test
    public void testOptions()
    {
        TestUtil.assertEquals("1bar", 
                "«test(1)»#test(int item)«item; foo=\"bar\"»#end\n#foo(Object it, String param): String\nreturn it == null ? \"\" : String.valueOf(it) + param;\n#end", 
                engine);
    }
    
    @Test
    public void testInlineExprWithSeparator()
    {
        TestUtil.assertEquals("1!,2!,3!", 
                "«[1,2,3]:Integer:item_detail(\"!\"); separator=\",\"»#item_detail(int item, String suffix)«item»«suffix»#end\n#separator(Object it, String param, int i)\n«if(i != 0)»«param»«endif»#end", 
                engine);
    }
    
    @Test
    public void testInlineExprPrimitiveWithSeparator()
    {
        TestUtil.assertEquals("1!,2!,3!", 
                "«[1,2,3]:int:item_detail(\"!\"); separator=\",\"»#item_detail(int item, String suffix)«item»«suffix»#end\n#separator(Object it, String param, int i)\n«if(i != 0)»«param»«endif»#end", 
                engine);
    }
    
    @Test
    public void testSeparator()
    {
        TestUtil.assertEquals("1!,2!,3!", 
                "«test([1,2,3])»#test(List<Integer> items)«items:Integer:item_detail(\"!\"); separator=\",\"»#end\n#item_detail(int item, String suffix)«item»«suffix»#end\n#separator(Object it, String param, int i)\n«if(i != 0)»«param»«endif»#end", 
                engine);
    }
    
    @Test
    public void testSeparatorLine()
    {
        TestUtil.assertEquals("1!\n2!\n3!\n", 
                "«test([1,2,3])»\n#test(List<Integer> items)«items:Integer:item_detail(\"!\"); separator=\"\\n\"»#end\n#item_detail(int item, String suffix)«item»«suffix»#end\n#separator(Object it, String param, int i)\n«if(i != 0)»«param»«endif»#end", 
                engine);
    }
    
    @Test
    public void testSeparatorLineIndent()
    {
        TestUtil.assertEquals("  @1!\n  @2!\n  @3!\n", 
                "«test([1,2,3])»\n#test(List<Integer> items)\n  «items:Integer:item_detail(\"!\"); separator=\"\\n\"»#end\n#item_detail(int item, String suffix)\n@«item»«suffix»\n#end\n#separator(Object it, String param, int i)\n«if(i != 0)»«param»«endif»#end", 
                engine);
    }
    
    // TODO
    /*@Test
    public void testImportedSeparatorLineIndent()
    {
        TestUtil.assertEquals("  @1!\n  @2!\n  @3!\n", 
                "«test([1,2,3])»\n#test(List<Integer> items)\n  «items:Integer:item_detail(\"!\"); separator=\"\\n\"»#end\n#item_detail(int item, String suffix)\n@«item»«suffix»\n#end\n#separator(Object it, String param, int i)\n«if(i != 0)»«param»«endif»#end", 
                engine);
    }*/

}
