package server;

import EZshare.argParser;
import EZshare.dataProc;
import EZshare.fileTransfer;
import client.EZshareClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ServerSocketFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class EZshareServer {
	public static String secret;
	public static ArrayList<JSONObject> serverRecord;
	public static HashMap<JSONObject,JSONObject> resourceMap=new HashMap<JSONObject,JSONObject>();
	public static String hostInfo;
	
      public static void main(String[] args){
    	  	JSONObject arguments=argParser.serverParser(args);
			//initiate the logger
			Logger log = Logger.getLogger(EZshareClient.class.getName());
			
			//set the logger level
			if((Boolean)arguments.get("debug")){
				log.setLevel(Level.ALL);
				log.setUseParentHandlers(false);
				ConsoleHandler handler = new ConsoleHandler();
				handler.setLevel(Level.ALL);
				log.addHandler(handler);
				log.info("setting debug on");
			}
			else{
				log.setLevel(Level.INFO);
			}
			//show the server information
			log.info("Starting the EZShare Server");
			secret=arguments.get("secret").toString();
    	  
    	  String hostname=arguments.get("advertisedhostname").toString();
    	  int port=Integer.parseInt((String)arguments.get("port"));
    	  hostInfo=hostname+":"+port;
    	  int exchangeInterval=Integer.parseInt(arguments.get("exchangeinterval").toString());
    		ServerSocketFactory factory = ServerSocketFactory.getDefault();
    		try(ServerSocket server = factory.createServerSocket(port)){
    			log.info("using secret:"+secret);
    			log.info("using advertised hostname:"+hostname);
    			log.info("bound to port:"+port);
    			log.info("started");
    			
    			
    		//periodically run the exchange command with other server
    			
    			 TimerTask tk=new TimerTask(){
     		    	public void run(){
     		    		if(serverRecord != null){
     		    		Random random=new Random();
     		    		JSONObject connect=serverRecord.get(random.nextInt(serverRecord.size()-1));
     		    		String connectHost=connect.get("hostname").toString();
     		    		int connectPort=Integer.parseInt(connect.get("port").toString());
     		    		try(Socket socket=new Socket(connectHost,connectPort)){
     		    			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
     		    			JSONObject exchange=new JSONObject();
     		    			JSONArray myList=new JSONArray();
     		    			myList.addAll(serverRecord);
     		    			exchange.put("command", "EXCHANGE");
     		    			exchange.put("serverList", myList);
     		    			output.writeUTF(exchange.toJSONString());
     		    		
     		    		} catch (UnknownHostException e) {
     		    			//delete the host if it does not respond
     		    			serverRecord.remove(connect);
     		    			
						} catch (IOException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
     		    		
     		    		}
     		    		
     		    }
     		    	};
     		   Timer timer=new Timer();
     		  timer.schedule(tk, exchangeInterval,exchangeInterval);
     		  
     		  
     		  
     		  
     		  
    			// Wait for connections.
    			while(true){
    				Socket client = server.accept();
    				
    				// Start a new thread for a connection
    				Thread t = new Thread(() -> {
						try {
							serveClient(client,resourceMap,serverRecord);
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
    				t.start();
    			}
    			

    		   
    		} catch (IOException e) {
    			e.printStackTrace();
    		}

      }
      
  	private static void serveClient(Socket client,HashMap<JSONObject,JSONObject> resourceMap,ArrayList<JSONObject> serverRecord) throws URISyntaxException{
  		
  		EZshareServer errorJudge = new EZshareServer(); 
  		
		try(Socket clientSocket = client){
			// Input stream
			DataInputStream input = new DataInputStream(clientSocket.
					getInputStream());
			// Output Stream
		    DataOutputStream output = new DataOutputStream(clientSocket.
		    		getOutputStream());
		    
		    JSONParser parser=new JSONParser();
		    
		    
		    JSONObject command = (JSONObject) parser.parse(input.readUTF());
		    System.out.println("command received:"+command.toJSONString());
		    JSONObject response = new JSONObject();
		    
		    
		    switch(command.get("command").toString()){
		    
		    //publish
		    case "PUBLISH":{
		    	JSONObject resource=(JSONObject)command.get("resource");
		    	
		    	if(errorJudge.rulesJudge(resource, resourceMap) == false){
		    		response.put("response", "error"); 
		    		response.put("errorMessage", "cannot publish resource");
		    	}else{
		    		if(resource.get("uri").toString().equals("")||errorJudge.uriIsfile(resource) == true 
		    				|| errorJudge.uriJudge(resource) == false){
		    			response.put("response", "error");
		    			response.put("errorMessage", "missing resource");
		    		}else{
		    			if(resource.get("owner").toString().equals("*")){
		    				response.put("response", "error");
			    			response.put("errorMessage", "invalid resource");
		    			}else{
		    				response =dataProc.publish(resource, resourceMap);
		    			}
		    		}
		    	}
		    	output.writeUTF(response.toJSONString());
		    	break;
		    }
		    
		    //remove
		    case "REMOVE":{
		    	JSONObject resource=(JSONObject)command.get("resource");
		    	if(resource.get("uri").toString().equals("")|| errorJudge.uriJudge(resource) == false){
		    		response.put("response", "error");
	    			response.put("errorMessage", "missing resource");
		    	}else{		    		
		    		response = dataProc.remove(resource, resourceMap);	    		
		    		
		    	}
		    	output.writeUTF(response.toJSONString());
		    	break;
		    }
		    
		    //share
		    case "SHARE" :{
		    	JSONObject resource=(JSONObject)command.get("resource");
		    	URI link;
		    	link = new URI(resource.get("uri").toString());
		    	String path = link.getPath();
		    	File f = new File(path);
		    	System.out.println(resource.get("uri").toString());
		    	if(f.exists() == false||command.get("secret").toString().equals("")||resource.get("uri").toString().equals("")
						||errorJudge.uriIsfile(resource)== false || errorJudge.uriJudge(resource) == false){
		    		response.put("response", "error");
	    			response.put("errorMessage", "missing resource and/or secret");
		    	}
		    	else{
		    		if(command.get("secret").toString().equals(secret) == false){
		    			response.put("response", "error");
			    		response.put("errorMessage", "incorrect secret");
		    			
		    		}else{
		    			if(errorJudge.rulesJudge(resource, resourceMap) == false){
		    				response.put("response", "error");
		    				response.put("errorMessage", "cannot share resource");
		    			}else{
		    				if(resource.get("owner").toString().equals("*")){
		    					response.put("response", "error");
				    			response.put("errorMessage", "invalid resource");
		    				}else{
		    					response=dataProc.share(resource, resourceMap);	
		    					
		    				}
		    			}
		    		}
		    	}
		    	output.writeUTF(response.toJSONString());
		    	break;
		    }
		    
		    //query
		    case "QUERY" :{
		    	JSONObject resourceTemp=(JSONObject)command.get("resource");
		    	JSONArray result=dataProc.query(resourceTemp, resourceMap);
		    	if((Boolean)command.get("relay")){
		    		command.replace("relay", false);
		    		//send message to other servers in serverRrcord
		    		if(serverRecord.isEmpty() == false){
		    			for(int i=0; i<serverRecord.size();i++){
		    				JSONObject connect = serverRecord.get(i);
		    				String connectHost = connect.get("hostname").toString();
		    				int connectPort = Integer.parseInt(connect.get("port").toString());
		    				try(Socket socket = new Socket(connectHost,connectPort)){
		    					DataOutputStream sOutput = new DataOutputStream(socket.getOutputStream());
		    					DataInputStream sInput = new DataInputStream(socket.getInputStream());
		    					sOutput.writeUTF(command.toJSONString());
		    					JSONArray sResult = (JSONArray) parser.parse(sInput.readUTF());
		    					result.addAll(sResult);
		    					
		    				}catch(IOException e){
		    				}
		    			}
		    		}
		    	}
		    	else{
		    		//result=dataProc.query(resourceTemp, resourceMap,hostInfo);
		    		output.writeUTF(result.toJSONString());
		    	}
		    	break;
		    }
		    
		    
		    //fetch
		    case "FETCH" :{
		    	
		    	
		    	break;
		    }
		    
		    //exchange
		    case "EXCHANGE" :{
		    	if(command.get("serverList").toString().isEmpty()){
		    		response.put("response", "error");
		    		response.put("errorMessage", "missing or invalid server list");
		    	}
		    	else{
		    	JSONArray servers=(JSONArray)command.get("serverList");
		    	ArrayList<JSONObject> serverList=new ArrayList<JSONObject>();
		    	for(int i=0;i<servers.size();i++){
		    		serverList.add((JSONObject)servers.get(i));
		    	}
		    	response=dataProc.exchange(serverList, serverRecord);
		    	}
		    	output.writeUTF(response.toJSONString());
		    	break;
		    }
		    }
		    
  			Set<JSONObject> Keyset = resourceMap.keySet();
  			System.out.println("keyset: "+Keyset);
			System.out.println("resourceMap: "+resourceMap);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
  	
  	
  	//Judge Publish
  	public boolean rulesJudge(JSONObject resource,HashMap<JSONObject,JSONObject> resourceMap){
  		
  		String channel = (String) resource.get("channel");
  		String uri = (String) resource.get("uri");
  		String owner = (String) resource.get("owner");
  		JSONObject key = new JSONObject();
  		key.put("channel", channel);
  		key.put("uri", uri);
  		key.put("owner", owner);
  		if(!resourceMap.isEmpty()){
  			
  			Set<JSONObject> Keyset = resourceMap.keySet(); 	  		
  	  		for(Iterator<JSONObject> it = Keyset.iterator(); it.hasNext();){
  	  			JSONObject keyInlist = it.next();
  	  			String KeyChannel = keyInlist.get("channel").toString();
  	  			String KeyUri = keyInlist.get("uri").toString();
  	  			String KeyOwner = keyInlist.get("owner").toString();
  	  			if(KeyChannel.equals(channel) && KeyUri.equals(uri)){
  	  				if(KeyOwner.equals(owner) == false){
  	  					return false;
  	  				}
  	  			}
  	  		}
  		}
  		
  		
  		return true;
  	}
  	
  	
  	//Judge Uri, if the shceme = file, return false, else return true.
  	public boolean uriIsfile(JSONObject resource){
  		
  		String uri = resource.get("uri").toString();
  		URI link;
  		
  		try {
			link = new URI(uri);
			String scheme = link.getScheme();
			if(scheme.equals("file") == true){
				return true;
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		
  		return false;
  	}
  	
  	//Judge if uri is absolute and empty
  	public boolean uriJudge(JSONObject resource){
  		
  		String uri = resource.get("uri").toString();
  		URI link;
  		try {
			link = new URI(uri);
			if(uri.equals("") || link.isAbsolute() == false){
				return false;
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		return true;
  	}
      
}
