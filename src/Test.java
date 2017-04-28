import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;


public class Test {
	public static void main(String[] args) throws Exception {
	/*	System.out.println(args[0]);
        File f = new File(args[0]);
        URI link;
        link = new URI(args[0]);
        System.out.println(link.getScheme());
        System.out.println(link.getPath());
        System.out.println();
        if(link.isAbsolute()){
        	System.out.println("true");
        }
        else  System.out.println("hh");
        if(f.exists()){
        	System.out.println("success");
        }
        else System.out.println("hh");
        */
		JSONObject key = new JSONObject();
		JSONObject value = new JSONObject();
		JSONObject resource = new JSONObject();
		
		
		HashMap<JSONObject,JSONObject> map = new HashMap<JSONObject,JSONObject>();
	
		key.put("owner", "jiao");
		key.put("channel", "001");
		key.put("uri", "file://Users/jyf/workspace_java/EZshare/1.jpg");
		value.put("name", "");
		value.put("description", "");
		value.put("ezserver", null);
		map.put(key, value);
		
		
		
	}
}
