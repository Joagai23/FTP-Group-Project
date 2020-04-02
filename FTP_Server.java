import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;

public class FTP_Server {

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
} // class CharacterServer
