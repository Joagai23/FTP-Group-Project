import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int connectionPort = 21;
    private int dataPort;
    public Socket connectionSocket;
    public Socket dataSocket;
    public ServerSocket serverConnectionSocket;
    public ServerSocket serverDataSocket;
    private BufferedReader inputConnectionSocket;
    private PrintWriter outputConnectionSocket;

    /***************************************************************/

    public Server()
    {
        connectionSocket = new Socket();
        try
        {
            serverConnectionSocket = new ServerSocket(connectionPort);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /***************************************************************/

    public int getConnectionPort()
    {
        return connectionPort;
    }

    public void setDataPort(int portNumber)
    {
        dataPort = portNumber;
    }

    public int getDataPort()
    {
        return dataPort;
    }

    public ServerSocket getServerConnectionSocket()
    {
        return serverConnectionSocket;
    }

    public ServerSocket getServerDataSocket()
    {
        return serverDataSocket;
    }

    public BufferedReader getInputConnectionSocket()
    {
        return inputConnectionSocket;
    }

    public PrintWriter getOutputConnectionSocket()
    {
        return outputConnectionSocket;
    }

    /***************************************************************/

    public void createServerSocket(ServerSocket socket, int portNumber)
    {
        try {
            socket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CreateWritersReaders()
    {
        // Get the inputConnectionSocket/outputConnectionSocket from the socket
        try {
            inputConnectionSocket = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            outputConnectionSocket = new PrintWriter(connectionSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
