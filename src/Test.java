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
		HashMap<JSONObject,JSONObject> map = new HashMap<JSONObject,JSONObject>();
		
		key.put("uri", "1");
		key.put("channel", "myChanne:1");
		key.put("owner", "1");
		value.put("name", "1");
		value.put("tag", "1");
		addNew(map,key, value);
		System.out.println(addNew(map,key, value));
		System.out.println("----------------");
		
		key.put("uri", "2");
		key.put("channel", "myChanne:2");
		key.put("owner", "2");
		value.put("name", "2");
		value.put("tag", "2");
		addNew(map,key, value);
		System.out.println(addNew(map,key, value));
		System.out.println("----------------");
		/*
		key.put("uri", "3");
		key.put("channel", "myChanne:3");
		key.put("owner", "3");
		value.put("name", "3");
		value.put("tag", "3");
		addNew(key, value);
		System.out.println(addNew(key, value));
		System.out.println("----------------");
		
		key.put("uri", "5");
		key.put("channel", "myChanne:4");
		key.put("owner", "4");
		value.put("name", "4");
		value.put("tag", "4");
		addNew(key, value);
		
		System.out.println(key.toString());
		System.out.println(value.toString());
		System.out.println("-------------------------------");
		System.out.println(key.get("uri"));
		
		System.out.println(addNew(key, value));
		System.out.println(addNew(key, value).keySet());
		System.out.println("-------------------------------");
		Set<JSONObject> keySet =  addNew(key, value).keySet();
		System.out.println(keySet.size());
		for(JSONObject jsonob : keySet){
			System.out.println(jsonob);
		}
		*/
    }
	
	public static HashMap<JSONObject, JSONObject> addNew (HashMap<JSONObject, JSONObject> map,JSONObject key, JSONObject value){
		//HashMap<JSONObject,JSONObject> map = new HashMap<JSONObject,JSONObject>();
		map.put(key, value);
		return map;
	}
}
