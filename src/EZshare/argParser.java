package EZshare;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//�������ת��JSON


public class argParser {

    /**
     * @param args the command line arguments
     */
	
	//generate a random string as the secret for server
	 public static String getRandomString(int length){
	     String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     Random random=new Random();
	     StringBuffer sb=new StringBuffer();
	     for(int i=0;i<length;i++){
	       int number=random.nextInt(62);
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	 }
	 
	 
	 
	    public static JSONObject serverParser(String[] args){
	    	Options options = new Options();
	    	options.addOption("advertisedhostname",true,"advertised hostname");
	    	options.addOption("connectionintervallimit",true,"connection interval limit in seconds");
	    	options.addOption("exchangeinterval",true,"exchange interval in seconds");
	    	options.addOption("port",true,"server port, an integer");
	    	options.addOption("secret",true,"secret");
	    	options.addOption("debug",false,"print debug information");
	    	
	    	CommandLineParser parser = new DefaultParser();
	        CommandLine cmd = null;

	        try{
	            cmd = parser.parse(options,args);      
	        } catch (ParseException e){
	            //help(options);
	        }
	        
	        JSONObject newCommand=new JSONObject();
	        if(cmd.hasOption("advertisedhostname")){
	        	newCommand.put("advertisedhostname", cmd.getOptionValue("advertisedhostname"));
	        }
	        else{
	        	try {
					newCommand.put("advertisedhostname",InetAddress.getLocalHost().getHostName());
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        if(cmd.hasOption("onnectionintervallimit")){
	        	newCommand.put("onnectionintervallimit", cmd.getOptionValue("onnectionintervallimit"));
	        }
	        else{
	        	newCommand.put("onnectionintervallimit","");
	        }
	        if(cmd.hasOption("exchangeinterval")){
	        	newCommand.put("exchangeinterval", cmd.getOptionValue("exchangeinterval"));
	        }
	        else{
	        	newCommand.put("exchangeinterval", "600");
	        }
	        if(cmd.hasOption("port")){
	        	newCommand.put("port", cmd.getOptionValue("port"));
	        }
	        else{
	        	newCommand.put("port", "3000");
	        }
	        if(cmd.hasOption("secret")){
	        	newCommand.put("secret", cmd.getOptionValue("secret"));
	        }
	        else{
	        	newCommand.put("secret",getRandomString(20));
	        }
	        if(cmd.hasOption("debug")){
	        	newCommand.put("debug", true);
	        }
	        else{
	        	newCommand.put("debug", false);
	        }
	        
	        return newCommand;
	    }
	    
	    
	    
	    
	    
	public static JSONObject clientParser(String[] args) {
        // TODO code application logic here
        
        
        Options options = new Options();

        options.addOption("channel",true,"channel");  //���ñ���
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
        //

    	options.addOption("relay",true,"resource URI");


        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try{
            cmd = parser.parse(options,args);      
        } catch (ParseException e){
            //help(options);
        }
        
        
        JSONObject newCommand=new JSONObject();
        JSONObject resource=new JSONObject();
        
        JSONArray serverList=new JSONArray();
        //GetResource
        
        
        //build the publish command
        if(cmd.hasOption("publish")){
        	
        	resource = argParser.GetResource(cmd);
        	
        	newCommand.put("command","PUBLISH");
        	newCommand.put("resource", resource);
        	
        //	System.out.println(newCommand.toJSONString());
        }
        
        
        //build the remove command
        if(cmd.hasOption("remove")){
        	
        	resource = argParser.GetResource(cmd);
        	
        	newCommand.put("command","REMOVE");
        	newCommand.put("resource", resource);
        	
        	//System.out.println(newCommand.toJSONString());
        	
        }
        
                      
        //build the Share command
        if(cmd.hasOption("share")){
        	
        	if(cmd.hasOption("secret")){
        		newCommand.put("secret", cmd.getOptionValue("secret"));
        	}
        	else {
        		newCommand.put("secret", "");
        	}
        	resource = argParser.GetResource(cmd);
        	
        
        newCommand.put("command","SHARE");
    	newCommand.put("resource", resource);   	
    //	System.out.println(newCommand.toJSONString());
    	
        }
        
        
        //build the Query command
        if(cmd.hasOption("query")){
        	
        		newCommand.put("relay", true); 
        	
        	resource = argParser.GetResource(cmd);
        	newCommand.put("command","QUERY");
        	newCommand.put("resourceTemplate", resource);   	
        //	System.out.println(newCommand.toJSONString());
        	
        }
        
        //build the Fetch command
        if(cmd.hasOption("fetch")){
        	
        	resource = argParser.GetResource(cmd);
        	newCommand.put("command","FETCH");
        	newCommand.put("resourceTemplate", resource);   	
        //	System.out.println(newCommand.toJSONString());
        	
        }
        
        
        //build the Exchange command
        if(cmd.hasOption("exchange")){
        	
        	if(cmd.hasOption("servers")){
        		
        		String[] ExchangeList = cmd.getOptionValue("servers").split(",");
        		
        		for(int i=0;i<ExchangeList.length;i++){
        			String[] str = ExchangeList[i].split(":");
        			JSONObject obj1 = new JSONObject();
        			obj1.put("hostname", str[0]);
        			obj1.put("port", str[1]);
        			serverList.add(obj1);
        		}
        		
        		newCommand.put("command","EXCHANGE");
        		newCommand.put("serverList", serverList);
        	//	System.out.println(newCommand.toJSONString());
        		           	        		
        	}
        	
        }
        
        if(cmd.hasOption("host")){
        	newCommand.put("host", cmd.getOptionValue("host"));
        }
        else{
        	//set the default host
        }
        
        if(cmd.hasOption("port")){
        	newCommand.put("port", cmd.getOptionValue("port"));
        }
        else{
        	//set the default port
        }
        
        if(cmd.hasOption("debug")){
        	newCommand.put("debug", true);
        }
        else{
        	newCommand.put("debug", false);
        }
        
        return newCommand;
        
    }
	
	
	
	
	public static JSONObject GetResource(CommandLine cmd){
		
		JSONObject getresource = new JSONObject();
		JSONArray list=new JSONArray();
		
		if(cmd.hasOption("name")){
    		getresource.put("name", cmd.getOptionValue("name"));
    	}
    	else{
    		getresource.put("name", "");
    	}
    	if(cmd.hasOption("description")){
    		getresource.put("description", cmd.getOptionValue("description"));
    	}
    	else{
    		getresource.put("description","");
    	}
    	if(cmd.hasOption("channel")){
    		getresource.put("channel", cmd.getOptionValue("channel"));
    	}
    	else{
    		getresource.put("channel", "");
    	}
    	if(cmd.hasOption("owner")){
    		getresource.put("owner", cmd.getOptionValue("owner"));
    	}
    	else{
    		getresource.put("owner","");
    	}
    	
    	//tag, array
    	if(cmd.hasOption("tags")){
    		String[] tagList=cmd.getOptionValue("tags").split(",");
    		
    		for(String tag:tagList){
    			list.add(tag);
    		}
    		getresource.put("tags", list);
    	}
    	else{
    		getresource.put("tags", "");
    	}
    	
    	if(cmd.hasOption("uri")){
    		getresource.put("uri", cmd.getOptionValue("uri"));
    	}
    	else{
    		getresource.put("uri", "");
    	}
    	// ezserver
    	getresource.put("ezserver", null);
		
		return getresource;
	}
	
}
