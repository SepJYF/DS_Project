package EZshare;


import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;






public class dataProc {
		
		//publish
		public static JSONObject publish(JSONObject resource,HashMap<JSONObject,JSONObject> resourceMap) throws ParseException{
			
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
			value.put("ezserver", resource.get("ezserver"));
			
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
			
			if(!resourceMap.containsKey(key)){
				response.put("response", "error");
				response.put("errorMessage", "cannot remove resource");
				
			}else{
				resourceMap.remove(key);
				response.put("response", "success");
			}


			return response;
		}
		
		
		//share
		public static JSONObject share(JSONObject resource,HashMap<JSONObject,JSONObject> resourceMap){
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
			value.put("ezserver", resource.get("ezserver"));
			
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
			
			if(resourceMap.containsKey(key)){
				JSONArray tagList = new JSONArray();
				tagList.addAll((JSONArray)resourceMap.get(key).get("tags"));
				dataProc tagJudge = new dataProc();
				if(tagJudge.tagsContain(tags, tagList) == true){
					String candiName = resourceMap.get(key).get("name").toString();
					String candiDesc = resourceMap.get(key).get("description").toString();
					String name = resource.get("name").toString();
					String description = resource.get("description").toString();
					if(candiName.contains(name) || candiDesc.contains(description) 
							|| (name.equals("") && description.equals(""))){
						
					}
				}
				
				
				
						
				
				
			}
			
			
			
			
			return response;
		}
		
		
		
		public static JSONObject fetch(JSONObject resource,HashMap<JSONObject,JSONObject> resourceMap) throws ParseException, URISyntaxException{
			
			System.out.println("*&&&&*--- go to the dataProcess fetch method~~~~***");
			HashMap<JSONObject,JSONObject> map=new HashMap<JSONObject,JSONObject>();
			JSONObject response = new JSONObject();
			JSONObject resResource = new JSONObject();
			int num = 0;
			
			for (JSONObject key : resourceMap.keySet()) {
	    		//this resource equals resourceTemplate
	    		if(resource.get("channel").toString().equals(key.get("channel"))&&resource.get("uri").toString().equals(key.get("uri"))){
	    			
	    			num++; 
	    			System.out.println("^^^^^  template match    ^^^^^ : "+num);
	    			
	    			URI link = new URI(resource.get("uri").toString());
	    			//String filePath = (String) resource.get("uri"); 
					String filePath = "/Users"+link.getPath();
					
					//check file exists
					File f = new File(filePath);
					if(f.exists()){
						System.out.println("------------test the getKey-----------"+key);
						JSONParser parser = new JSONParser();
						
						map.put(key, resourceMap.get(key));
						//resResource.put(key, resourceMap.get(key));
						//resResource.put("RESOURCE", map);
						resResource.put("resourceSize", Long.toString(f.length()));
						
						System.out.println(" ");
						
						System.out.println("======response resource======"+resResource);
						
					}	
	    		}
	    	}
			
			if(num>0){
				response = resResource;  //?
				response.put("resultSize", num);
				
				
			}else{
				response.put("response", "error");
				response.put("errorMessage","invalid resourceTemplate");
			}
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
	  	
	  	//query: Judge any tags present in the template are also present in the candidate
	  	public boolean tagsContain(ArrayList tags, ArrayList tagList){
	  		
	  		for(int i=0; i<tags.size();i++){
				if(tagList.contains(tags.get(i)) == false){
					return false;
				}
			}
	  		return true;
	  		
	  	}
	  	
		
		
		
		
}
