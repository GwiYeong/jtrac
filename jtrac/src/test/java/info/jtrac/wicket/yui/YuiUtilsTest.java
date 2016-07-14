package info.jtrac.wicket.yui;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

public class YuiUtilsTest extends TestCase {
    
    public void testJsonConversion() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("foo", true);
        map.put("bar", false);        
	String result = YuiUtils.getJson(map);
	assertTrue(result.indexOf("foo : true") != -1);
	assertTrue(result.indexOf("bar : false") != -1);
	assertTrue(result.indexOf("{") == 0);
	assertTrue(result.indexOf("}") == result.length() -1);
	assertTrue(result.length() == "{foo : true, bar : false}".length());
    }
    
}
