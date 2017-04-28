package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.net.ServerSocketFactory;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import EZshare.fileTransfer;


public class TestServer {
	
	// Declare the port number
	private static int port = 3000;
	
	// Identifies the user number connected
	private static int counter = 0;
	
	private static String mySecret = "mySecret";

	public static void main(String[] args) {
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		try(ServerSocket server = factory.createServerSocket(port)){
			System.out.println("Waiting for client connection..");
			
			// Wait for connections.
			while(true){
				Socket client = server.accept();
				counter++;
				System.out.println("Client "+counter+": Applying for connection!");
				
				
				// Start a new thread for a connection
				Thread t = new Thread(() -> serveClient(client));
				t.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	private static void serveClient(Socket client){
		try(Socket clientSocket = client){
			
			// The JSON Parser
			JSONParser parser = new JSONParser();
			// Input stream
			DataInputStream input = new DataInputStream(clientSocket.
					getInputStream());
			// Output Stream
		    DataOutputStream output = new DataOutputStream(clientSocket.
		    		getOutputStream());
		    System.out.println("CLIENT: "+input.readUTF());
		    
		    // Receive more data..
		    while(true){
		    	if(input.available() > 0){
		    		System.out.println("COMMAND RECEIVED: ");
		    		
		    		
		    		// Attempt to convert read data to JSON
		    		JSONObject command = (JSONObject) parser.parse(input.readUTF());
		    		System.out.println("COMMAND RECEIVED: "+command.toJSONString());
		    		//Integer result = parseCommand(command,output);
		    		if(command.get("command").equals(("PUBLISH"))){
			    		JSONObject results = new JSONObject();
			    		results.put("response","success" );
			    		output.writeUTF(results.toJSONString());
			    		results.put("response","2" );
			    		output.writeUTF(results.toJSONString());
			    		
		    		}
		    		if(command.get("command").equals(("REMOVE"))){
			    		JSONObject results = new JSONObject();
			    		results.put("response","success" );
			    		output.writeUTF(results.toJSONString());
			    		
		    		}
		    		if(command.get("command").equals(("QUERY"))||
		    				command.get("command").equals(("EXCHANGE"))){
			    		JSONObject results = new JSONObject();
			    		results.put("response","success" );
			    		output.writeUTF(results.toJSONString());
			    		//add the xml write in method 
		    		}
		    		if(command.get("command").toString().equals("FETCH")){
		    			System.out.println("jlksjdfl");
		    			fileTransfer.send(command,output);
					}
		    		if(command.get("command").equals("SHARE")){
		    			System.out.println("that's it");
						share(command,input,output);
					}
		    	}
		    }
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	private static void share(JSONObject command, DataInputStream input, DataOutputStream output) {
		// TODO Auto-generated method stub
		System.out.println("share method is called");
		try {
			//simple for test
			if(command.containsKey("secret")){
				if(command.get("secret").equals(mySecret)){
					System.out.println("hey");
	
					JSONObject response = new JSONObject();
					response.put("command","shareAccept");
					output.writeUTF(response.toJSONString());
					
					String result = input.readUTF();
					JSONParser parser = new JSONParser();
					System.out.println(result);
					
					JSONObject cmd = (JSONObject) parser.parse(result);
					String filePath = ("Server_file/0.jpg");
					RandomAccessFile uploadingFile = new RandomAccessFile(filePath,"rw");
					long fileSizeRemaining = (Long) cmd.get("resourceSize");
					int chunkSize = setChunkSize(fileSizeRemaining);
					byte[] receiveBuffer = new byte[chunkSize];
					int num;
					
					System.out.println("Receiving file..."+fileSizeRemaining);
					while((num=input.read(receiveBuffer))>0){
						//write the received bytes to the file
						uploadingFile.write(Arrays.copyOf(receiveBuffer, num));
						
						fileSizeRemaining -= num;
						chunkSize = setChunkSize(fileSizeRemaining);
						receiveBuffer = new byte[chunkSize];
						
						if(fileSizeRemaining==0){
							break;
						}
					
					System.out.println("Successfully upload");
					uploadingFile.close();
					}
			}
			else{
				output.writeUTF("Please enter the right secret.");
			}
			}
		} catch (IOException | ParseException e){
			e.printStackTrace();
		}
	}

	private static int setChunkSize(long fileSizeRemaining) {
		//determine the chunksize
				int chunkSize = 1024;
				
				if(fileSizeRemaining<chunkSize){
					chunkSize = (int) fileSizeRemaining;
				}
				return chunkSize;
	}

}
