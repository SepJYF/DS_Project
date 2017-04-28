package EZshare;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import org.json.simple.JSONObject;

//fileTransfer class, dealing with the file transferring
public class fileTransfer {

	//send method, returns the size of file to be sent
	public static void send(JSONObject command,DataOutputStream output){
		System.out.println("jlksjdfl");
		try{	
			JSONObject response = new JSONObject();
			String filePath = (String) command.get("uri"); 
			
			//check file exists
			File f = new File(filePath);
			if(f.exists()){
				
				response.put("response", "success");
				response.put("resourceSize",Long.toString(f.length()));
				RandomAccessFile byteFile=new RandomAccessFile(f,"r");
				byte[] sendingBuffer = new byte[1024*1024];
				int num;
				while((num = byteFile.read(sendingBuffer)) > 0){
					System.out.println(num);
					output.write(Arrays.copyOf(sendingBuffer, num));
				}
				byteFile.close();
				
			}else{
				
				response.put("response", "error");
				response.put("errorMessage", "invalid resourceTemplate");
			}
			
			output.writeUTF(response.toJSONString());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void receive(JSONObject response,DataInputStream input){
		
		File f=new File("src/");
		try {
			RandomAccessFile downloadingFile = new RandomAccessFile(f, "rw");
			long fileSizeRemaining = (Long) response.get("resourceSize");
			int chunkSize = setChunkSize(fileSizeRemaining);
			
			// Represents the receiving buffer
			byte[] receiveBuffer = new byte[chunkSize];
			
			// Variable used to read if there are remaining size left to read.
			int num;

			System.out.println("Downloading "+" of size "+fileSizeRemaining);
			while((num=input.read(receiveBuffer))>0){
				// Write the received bytes into the RandomAccessFile
				downloadingFile.write(Arrays.copyOf(receiveBuffer, num));
				
				// Reduce the file size left to read..
				fileSizeRemaining-=num;
				
				// Set the chunkSize again
				chunkSize = setChunkSize(fileSizeRemaining);
				receiveBuffer = new byte[chunkSize];
				
				// If you're done then break
				if(fileSizeRemaining==0){
					break;
				}
			}
			System.out.println("File received!");
			downloadingFile.close();
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static int setChunkSize(long fileSizeRemaining){
		// Determine the chunkSize
		int chunkSize=1024*1024;
		
		// If the file size remaining is less than the chunk size
		// then set the chunk size to be equal to the file size.
		if(fileSizeRemaining<chunkSize){
			chunkSize=(int) fileSizeRemaining;
		}
		
		return chunkSize;
	}
}
