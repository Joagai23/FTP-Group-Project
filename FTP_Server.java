import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;

public class FTP_Server {
	
	private static String logFile = "serverLog.txt";

    public static void main(String args[]) {

        Server server = new Server();
        ServerOptions serverOptions = new ServerOptions(server);
        boolean serverControl = true;
        boolean connectionControl = false;
        
        //BufferedReader input;
        //PrintWriter output;

        String data = "";

        while(true)
        {
            try
            {
                System.out.println("Character Server waiting for requests");
                // Accept a connection  and create the socket for the transmission with the client
                server.connectionSocket = server.serverConnectionSocket.accept();
                connectionControl = true;
                System.out.println("Connection accepted");
				server.CreateWritersReaders();

                while(connectionControl)
                {
                    // Read the data sent by the client
                    data =  server.getInputConnectionSocket().readLine();
                    System.out.println("Server receives: " + data);
                    // Convert text to Upper Case
                    //data = data.toUpperCase();
                    // Send the text
                    //data = "220 Service ready for new user";
                    serverOptions.readOption(data);
                    
                    
                    registerAction("test", data);
                    
                    server.getOutputConnectionSocket().println(data);
                    
                    System.out.println("Server sends: " + data);

                    if(data.compareTo("QUIT") == 0)
                    {
                        connectionControl = false;
                    }
                }

                // Close the socket
                server.connectionSocket.close();

            } catch(IOException e)
            {
                System.out.println("Error: " + e);
            }
        }
        // Close the server socket
        //server.getServerConnectionSocket().close();
    } // main
    
	
	public static void registerAction(String user, String command) {
	    
	    LocalDateTime date = LocalDateTime.now();
	    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	    String formattedDate = date.format(dateFormat);
	    
	    try {
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(logFile, true));
			
			fileWriter.newLine();
			fileWriter.write("User: " + user + " - " + formattedDate + " - " + command);
			
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
    
    
} // class CharacterServer
