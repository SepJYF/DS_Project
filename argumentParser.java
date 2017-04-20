package EZshare;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



public class argumentParser {

    /**
     * @param args the command line arguments
     */
    
	public static void main(String[] args) {
        // TODO code application logic here
        
        
            Options options = new Options();

        options.addOption("channel",true,"channel");
        options.addOption("debug",false, "print debug information");
        options.addOption("description",true,"resource description");
        options.addOption("exchange",false,"exchange server list with server");
        options.addOption("fetch",false,"fetch resource from server");
        options.addOption("host",true,"server host, a domain name or IP address");
        options.addOption("name",true,"resouce name");
        options.addOption("owner",true,"owner");
        options.addOption("port",true,"server port, an integer");
        options.addOption("publish",false,"publish resource on server");
        options.addOption("query",false,"query for resources from server");
        options.addOption("remove",false,"remove resource from server");
        options.addOption("secret",true,"secret");
        options.addOption("servers",true,"server list, host1:port1,host2:port2,...");
        options.addOption("share",false,"share resource on server");
        options.addOption("tags",true,"resource tags, tag1,tag2,tag3,...");
        options.addOption("uri",true,"resource URI");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try{
            cmd = parser.parse(options,args);      
        } catch (ParseException e){
            //help(options);
        }
        
        
        JSONObject newCommand=new JSONObject();
        JSONObject resource=new JSONObject();
        JSONArray list=new JSONArray();
        //build the publish command
        if(cmd.hasOption("publish")){
        	
        	
        	if(cmd.hasOption("name")){
        		resource.put("name", cmd.getOptionValue("name"));
        	}
        	else{
        		resource.put("name", "");
        	}
        	if(cmd.hasOption("description")){
        		resource.put("description", cmd.getOptionValue("description"));
        	}
        	else{
        		resource.put("description","");
        	}
        	if(cmd.hasOption("channel")){
        		resource.put("channel", cmd.getOptionValue("channel"));
        	}
        	else{
        		resource.put("channel", "");
        	}
        	if(cmd.hasOption("owner")){
        		resource.put("owner", cmd.getOptionValue("owner"));
        	}
        	else{
        		resource.put("owner","");
        	}
        	if(cmd.hasOption("tags")){
        		String[] tagList=cmd.getOptionValue("tags").split(",");
        		
        		for(String tag:tagList){
        			list.add(tag);
        		}
        		resource.put("tags", list);
        	}
        	else{
        		resource.put("tags", "");
        	}
        	
        	if(cmd.hasOption("uri")){
        		resource.put("uri", cmd.getOptionValue("uri"));
        	}
        	else{
        		resource.put("uri", "");
        	}
        	resource.put("ezserver", null);
        	newCommand.put("resource", resource);
        	newCommand.put("command","publish");
        	System.out.println(newCommand.toJSONString());
        }
        
        //build the remove command
        if(cmd.hasOption("remove")){
        	if(cmd.hasOption("name")){
        		resource.put("name", cmd.getOptionValue("name"));
        	}
        	else{
        		resource.put("name", "");
        	}
        }
        
        

        
    }
}
