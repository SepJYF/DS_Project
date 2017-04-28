package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import EZshare.argParser;
import EZshare.fileTransfer;

public class EZshareClient {
	// IP and port
	//	private static String ip = "sunrise.cis.unimelb.edu.au";
		
		public static void main(String[] args) throws ParseException {
			
			//initiate the logger
			Logger log = Logger.getLogger(EZshareClient.class.getName());
			
			//set the logger level
			JSONObject arguments=argParser.clientParser(args);
			if((Boolean)arguments.get("debug")){
				log.setLevel(Level.ALL);
				log.setUseParentHandlers(false);
				ConsoleHandler handler = new ConsoleHandler();
				handler.setLevel(Level.ALL);
				log.addHandler(handler);
				log.info("setting debug on");
			}
			else{
				log.setLevel(Level.OFF);
			}
			
			
			int port=Integer.parseInt((String)arguments.get("port"));
			String ip=(String)arguments.get("host");
		//	System.out.println(ip);
			arguments.remove("host");
			arguments.remove("port");
			arguments.remove("debug");  
			try(Socket socket = new Socket(ip, port)){
				System.out.println("1 step");
				// Output and Input Stream
				DataInputStream input = new DataInputStream(socket.
						getInputStream());
			    DataOutputStream output = new DataOutputStream(socket.
			    		getOutputStream());
			    
		    	output.writeUTF(arguments.toJSONString());
		    	log.fine("SENT:"+arguments.toJSONString());
		    	output.flush();
		    	System.out.println("step 2");
		    	
			    while(true){
	                        if(input.available() > 0) {
	                        	
	                        	String message=input.readUTF();
	                        	
	                        	JSONParser parser = new JSONParser();
	                        	JSONObject response = (JSONObject) parser.parse(message);
	                        	
	                        	/*if(response.containsKey("resourceSize")){
	                        		
	                        		System.out.println("03"+response);
	                        		fileTransfer.receive(response, input);
	                        	}*/
	                        	if(arguments.containsKey("fetch")){
	                        		fileTransfer.receive(response, input);
	                        	}
	                        	
	                        	log.fine("RECEIVED:"+message);
	                        	System.out.println(message);
	                        }
		    		
			    }
			    
			} catch (UnknownHostException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				
			}

		}
}
