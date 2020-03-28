import java.net.*;
import java.io.*;

public class FTP_Server {

    public static void main(String args[]) {

        Server server = new Server();
        boolean serverControl = true;
        boolean connectionControl = false;
        BufferedReader input;
        PrintWriter output;

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

                while(connectionControl)
                {
                    // Get the input/output from the socket
                    input = new BufferedReader(new InputStreamReader(server.connectionSocket.getInputStream()));
                    output = new PrintWriter(server.connectionSocket.getOutputStream(), true);

                    // Read the data sent by the client
                    data =  input.readLine();
                    System.out.println("Server receives: " + data);
                    // Convert text to Upper Case
                    data = data.toUpperCase();
                    // Send the text
                    //data = "220 Service ready for new user";
                    output.println(data);
                    System.out.println("Server sends: " + data);

                    if(data.compareTo("END") == 0)
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