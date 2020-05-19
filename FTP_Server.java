import java.io.*;

public class FTP_Server {

    public static void main(String args[]) {

        Server server = new Server();
        ServerOptions serverOptions = new ServerOptions(server);

        boolean connectionControl = false;

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

                serverOptions.sendCodeMessage(220);

                while(connectionControl)
                {
                    // Read the data sent by the client
                    data =  server.getInputCommandSocket().readLine();
                    System.out.println("Server receives: " + data);

                    option = ServerOptions.getOption(data);

                    ServerOptions.readOption(option);

                    ServerOptions.registerAction("test", data);

                    if(option.compareTo("PURT") != 0) {

                        if(option.compareTo("QUIT") == 0)
                        {
                            connectionControl = false;
                        }
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
