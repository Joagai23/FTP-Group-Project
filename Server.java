import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int commandPort = 21;
    private int dataPort;
    public Socket commandSocket;
    public Socket dataSocket;
    public ServerSocket serverConnectionSocket;
    public ServerSocket serverDataSocket;
    private BufferedReader inputCommandSocket;
    private PrintWriter outputCommandSocket;

    /***************************************************************/

    public Server()
    {
        commandSocket = new Socket();
        try
        {
            serverConnectionSocket = new ServerSocket(commandPort);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /***************************************************************/

    public int getCommandPort()
    {
        return commandPort;
    }

    public void setDataPort(int portNumber)
    {
        dataPort = portNumber;
        System.out.println("Data port number set to: " + portNumber);
        outputCommandSocket.println("OK");
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

    public BufferedReader getInputCommandSocket()
    {
        return inputCommandSocket;
    }

    public PrintWriter getOutputCommandSocket()
    {
        return outputCommandSocket;
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

    public void createCommandWritersReaders()
    {
        // Get the inputCommandSocket/outputCommandSocket from the socket
        try {
            inputCommandSocket = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
            outputCommandSocket = new PrintWriter(commandSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDataSocket()
    {
        dataSocket = new Socket();
        try
        {
            serverDataSocket = new ServerSocket(dataPort);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
