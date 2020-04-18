import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FTP_Server {

    private static String logFile = "serverLog.txt";

    public static void main(String args[]) {

        Server server = new Server();
        ServerOptions serverOptions = new ServerOptions(server);
        boolean hasDataPortNumber = false;
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
                    // Convert text to Upper Case
                    //data = data.toUpperCase();
                    // Send the text
                    //data = "220 Service ready for new user";
                   
                    serverOptions.readOption(getOption(data));

                    registerAction("test", data);

                    server.getOutputCommandSocket().println(data);
                    
                    System.out.println("Server sends: " + data);

                    if(data.compareTo("QUIT") == 0)
                    {
                        connectionControl = false;
                        ServerOptions.sendCodeMessage(221, server.getOutputCommandSocket());
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
    
    /**
     * It receives the full command send by client and returns just the option selected
     * 
     * @param clientCommand
     * @return
     */
    public static String getOption(String clientCommand) {
    	String option = "";
    	String[] parts;
    	
    	parts = clientCommand.split(" |\\\\");
    	option = parts[0];
    	
    	return option;
    }

} // class CharacterServer
