import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int connectionPort = 21;
    private int dataPort;
    public Socket connectionSocket;
    public Socket dataSocket;
    public ServerSocket serverConnectionSocket;
    public ServerSocket serverDataSocket;

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

    /***************************************************************/

    public void createServerSocket(ServerSocket socket, int portNumber)
    {
        try {
            socket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
