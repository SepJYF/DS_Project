package EZshare;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;






public class dataProc {
		
		//publish
		public static JSONObject publish(JSONObject resource,HashMap<JSONObject,JSONObject> resourceMap,
				String serverInfo) throws ParseException{
			
			JSONObject response=new JSONObject();
			JSONObject key=new JSONObject();
			JSONObject value=new JSONObject();
			JSONArray tags=new JSONArray();
			 
			//Remove the whitespace
			dataProc newStr = new dataProc();
			String nameStr = resource.get("name").toString();
			nameStr = newStr.removeWhitespace(nameStr);
			resource.replace("name", nameStr);
			String desStr = resource.get("description").toString();
			desStr = newStr.removeWhitespace(desStr);
			resource.replace("description", desStr);
			 
			 
			key.put("owner",resource.get("owner"));
			key.put("channel", resource.get("channel"));
			key.put("uri",resource.get("uri"));
			
			if(resource.get("tags").toString().isEmpty()){
				tags.add("");
			}
			else{
			tags.addAll((JSONArray)resource.get("tags"));
			}
			
			value.put("tags", tags);
			value.put("description",resource.get("description"));
			value.put("name", resource.get("name"));
			value.put("ezserver", serverInfo);
			
			//rewrite or publish
			if(resourceMap.containsKey(key) == true){
				resourceMap.replace(key, value);
			}else{
				resourceMap.put(key, value);
			}
			
			response.put("response", "success");
			return response;
		}
		
		
		//remove
		public static JSONObject remove(JSONObject resource,HashMap<JSONObject,JSONObject> resourceMap){
			JSONObject response=new JSONObject();
			JSONObject key=new JSONObject();
			key.put("owner",resource.get("owner"));
			key.put("channel", resource.get("channel"));
			key.put("uri",resource.get("uri"));
			
			if(resourceMap.containsKey(key) == false){
				response.put("response", "error");
				response.put("errorMessage", "cannot remove resource");
				
			}else{
				resourceMap.remove(key);
				response.put("response", "success");
			}
			
			return response;
		}
		
		
		//share
		public static JSONObject share(JSONObject resource,HashMap<JSONObject,JSONObject> resourceMap,String serverInfo){
			JSONObject response=new JSONObject();
			JSONObject key=new JSONObject();
			JSONObject value=new JSONObject();
			JSONArray tags=new JSONArray();
			 
			//Remove the whitespace
			dataProc newStr = new dataProc();
			String nameStr = resource.get("name").toString();
			nameStr = newStr.removeWhitespace(nameStr);
			resource.replace("name", nameStr);
			String desStr = resource.get("description").toString();
			desStr = newStr.removeWhitespace(desStr);
			resource.replace("description", desStr);
			 
			 
			key.put("owner",resource.get("owner"));
			key.put("channel", resource.get("channel"));
			key.put("uri",resource.get("uri"));
			
			if(resource.get("tags").toString().isEmpty()){
				tags.add("");
			}
			else{
			tags.addAll((JSONArray)resource.get("tags"));
			}
			
			value.put("tags", tags);
			value.put("description",resource.get("description"));
			value.put("name", resource.get("name"));
			value.put("ezserver", serverInfo);
			
			//rewrite or share
			if(resourceMap.containsKey(key) == true){
				resourceMap.replace(key, value);
			}else{
				resourceMap.put(key, value);
			}
			
			response.put("response", "success");
			return response;

		}
		
		
		//query
		public static JSONArray query(JSONObject resource,HashMap<JSONObject,JSONObject> resourceMap){
			JSONArray response=new JSONArray();
			return response;
		}
		
		//check the channel match
		public static JSONObject fetch(JSONObject resource,HashMap<JSONObject,JSONObject> resourceMap) throws ParseException{
			String myChannel = resource.get("channel").toString();
			String myUri = resource.get("uri").toString();
			JSONObject response = new JSONObject();
			JSONObject resResource = new JSONObject();
			
			ArrayList<JSONObject> keySet = (ArrayList) resourceMap.keySet();
			ArrayList<JSONObject> keyList = keySet;
			int num = 0;
			for(int i=0;i<keyList.size();i++){
				String keyChannel = (String) keyList.get(i).get("channel");
				String keyUri = (String) keyList.get(i).get("uri");
				if(keyChannel.equals(myChannel)&&keyUri.equals(myUri)){
					num++;        												//count the resultSize
					JSONObject key = keyList.get(i);
					resResource.put("response", "success");
					JSONParser parser = new JSONParser();
					resResource.put("resource",parser.parse(key.toString()+resourceMap.get(key).toString()));
					return resResource;
				}	
			}
			
			response.put("response", "error");
			response.put("errorMessage","invalid resourceTemplate");
			return response;
		}
		
		public static JSONObject exchange(ArrayList<JSONObject> serverList,ArrayList<JSONObject> serverRecord){
			JSONObject response=new JSONObject();
			serverRecord.removeAll(serverList);
			serverRecord.addAll(serverList);
			response.put("response", "success");
			
			return response;
			
		}
		
		
		//remove the Whitespace in the begin and end of string
	  	public String removeWhitespace(String str){
	  		
	  		if(str.isEmpty() == false){
	  			if(Character.isWhitespace(str.charAt(0)) == true 
	  					|| Character.isWhitespace(str.charAt(str.length()-1)) == true){
	  				str = str.replaceFirst("\\s", "");
	  				str = str.substring(0, str.length()-1);
	  			}
	  		}
	  		
	  		return str;
	  	}
		
		
		
		
}
