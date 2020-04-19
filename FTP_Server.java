import java.io.*;

public class FTP_Server {

    public static void main(String args[]) {

        Server server = new Server();

        boolean hasDataPortNumber = false;
        boolean connectionControl = false;
        //BufferedReader input;
        //PrintWriter output;

        String data = "";
        String option = "";

        while(true)
        {
            try
            {
                System.out.println("Character Server waiting for requests");
                // Accept a connection  and create the socket for the transmission with the client
                server.commandSocket = server.serverConnectionSocket.accept();
                connectionControl = true;
                System.out.println("Connection accepted");
                server.createCommandWritersReaders();

                int dataPort = -1;

                // Get data port number from the client and update value in server
                while(dataPort == -1)
                {
                    //put time out
                    dataPort = Integer.parseInt(server.getInputCommandSocket().readLine());
                }

                server.setDataPort(dataPort);

                while(connectionControl)
                {
                    // Read the data sent by the client
                    data =  server.getInputCommandSocket().readLine();
                    System.out.println("Server receives: " + data);

                    option = ServerOptions.getOption(data);

                    ServerOptions.readOption(option);

                    ServerOptions.registerAction("test", data);

                    server.getOutputCommandSocket().println(option);

                    System.out.println("Server sends: " + option);

                    if(option.compareTo("QUIT") == 0)
                    {
                    	ServerOptions.sendCodeMessage(221);
                        connectionControl = false;
                    }
                }

                // Close the socket
                server.commandSocket.close();

            } catch(IOException e)
            {
                System.out.println("Error: " + e);
            }
        }
        // Close the server socket
        //server.getServerConnectionSocket().close();
    } // main
} // class CharacterServer
