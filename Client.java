import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private int connectionPort = 0;
    private final int dataPort = 22;
    private String configFile = "config.txt";
    private final String IP = "localhost";
    private Socket connectionSocket;
    private Socket dataSocket;
    private BufferedReader inputConnectionSocket;
    private PrintWriter outputConnectionSocket;
    private BufferedReader inputDataSocket;
    private PrintWriter outputDataSocket;

    /***************************************************************/

    public Client()
    {
    	
    	getPortFromConfigFile();
    	establishConnection();
    	CreateWritersReaders();
    	
    	/*
        try {
            // Establish connection with Server via connection socket
            connectionSocket = new Socket("localhost", 21);

            CreateWritersReaders();
            // Send socket port number for data socket

        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    /***************************************************************/

    public int getConnectionPort()
    {
        return connectionPort;
    }

    public int getDataPort()
    {
        return dataPort;
    }

    public void setConnectionSocket(Socket connectionSocket)
    {
        this.connectionSocket = connectionSocket;
    }

    public Socket getConnectionSocket()
    {
        return connectionSocket;
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

    private void CreateWritersReaders()
    {
        // Get the inputConnectionSocket/outputConnectionSocket from the socket
        try {
            inputConnectionSocket = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            outputConnectionSocket = new PrintWriter(connectionSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void getPortFromConfigFile() {
    	
    	try {
			BufferedReader fileReader = new BufferedReader(new FileReader(configFile));
			try {
				connectionPort = Integer.parseInt(fileReader.readLine());
				System.out.println(connectionPort);
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
			connectionSocket = new Socket(IP, connectionPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
