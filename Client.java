import java.net.Socket;

public class Client {

    private final int connectionPort = 21;
    private int dataPort;
    private Socket connectionSocket;
    private Socket dataSocket;

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

    /***************************************************************/
}
