import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private int commandPort = 0;
    private final int dataPort = 22;
    private String configFile = "config.txt";
    private final String IP = "localhost";
    private Socket commandSocket;
    private Socket dataSocket;
    private BufferedReader inputConnectionSocket;
    private PrintWriter outputConnectionSocket;
    private BufferedReader inputDataSocket;
    private PrintWriter outputDataSocket;

    /***************************************************************/

    public Client()
    {
        setPortFromConfigFile();
        establishConnection();
        createCommandWritersReaders();
        sendDataPortNumber();


    	/*
        try {
            // Establish connection with Server via connection socket
            commandSocket = new Socket("localhost", 21);
            createCommandWritersReaders();
            // Send socket port number for data socket
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    /***************************************************************/

    public int getCommandPort()
    {
        return commandPort;
    }

    public int getDataPort()
    {
        return dataPort;
    }

    public void setCommandSocket(Socket commandSocket)
    {
        this.commandSocket = commandSocket;
    }

    public Socket getCommandSocket()
    {
        return commandSocket;
    }

    public void setDataSocket(Socket dataSocket)
    {
        this.dataSocket = dataSocket;
    }

    public Socket getDataSocket()
    {
        return dataSocket;
    }

    public BufferedReader getInputConnectionSocket()
    {
        return inputConnectionSocket;
    }

    public BufferedReader getInputDataSocket()
    {
        return inputDataSocket;
    }

    public PrintWriter getOutputConnectionSocket()
    {
        return outputConnectionSocket;
    }

    public PrintWriter getOutputDataSocket()
    {
        return outputDataSocket;
    }

    /***************************************************************/

    private void createCommandWritersReaders()
    {
        // Get the inputConnectionSocket/outputConnectionSocket from the socket
        try {
            inputConnectionSocket = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
            outputConnectionSocket = new PrintWriter(commandSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPortFromConfigFile() {

        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(configFile));
            try {
                commandPort = Integer.parseInt(fileReader.readLine());
                System.out.println(commandPort);
                fileReader.close();
            } catch (NumberFormatException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void establishConnection() {

        try {
            commandSocket = new Socket(IP, commandPort);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void sendDataPortNumber()
    {
        outputConnectionSocket.println(dataPort);
    }
    
    private String getLocalhost() {
    	String localhost = "";
		try {
			localhost = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return localhost;
    }
    
    private String getCommandPortConnection() {
    	String command = "";
    	String hostport = getLocalhost();
    	
    	command = command.concat("PORT").concat(" ").concat(hostport).concat("\\r\\n");
    	
    	return command;	
    }
}
