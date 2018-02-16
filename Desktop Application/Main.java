import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

private static ServerSocket serverSocket;
private static Socket clientSocket;
private static InputStreamReader inputStreamReader;
private static BufferedReader bufferedReader;
private static String message;
private final String START_INDICATOR = "///"; // "///"
private final String END_INDICATOR = "\\\\\\"; // "\\\"
private boolean writting = false;

public Main(){
	FileOutputStream out = null;

    try {
		
        serverSocket = new ServerSocket(4444);  //Server socket
    } catch (IOException e) {
        System.out.println("Could not listen on port: 4444");
    }

    System.out.println("Server started. Listening to the port 4444");

	
    while (true) {
        try {
			
            clientSocket = serverSocket.accept();   //accept the client connection
            inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader); //get client msg                    
            message = "";
			String line;
			while ((line = bufferedReader.readLine()) != null){
				message += line;
				
				System.out.println(line + "\n");
				if(line.trim().equals(END_INDICATOR)){
					System.out.println("saw end indicator");
					writting = false;
					if (out != null) {
						out.close();
					}
				}
				if(writting){
					if (out != null) {
						out.write((line + "\n").getBytes());
					}
				}
				if(line.trim().equals(START_INDICATOR)){
					writting = true;
					out = new FileOutputStream("output.csv");
					System.out.println("saw start indicator");
				}
			}
            //System.out.println(message);
            

        } catch (IOException ex) {
            System.out.println("Problem in message reading");
        }
		
    }

     }
	public static void main(String[] args) {
		Main m = new Main();
	
	}  
}