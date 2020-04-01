import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private final int connectionPort = 21;
    private final int dataPort = 22;
    private Socket connectionSocket;
    private Socket dataSocket;
    private BufferedReader inputConnectionSocket;
    private PrintWriter outputConnectionSocket;
    private BufferedReader inputDataSocket;
    private PrintWriter outputDataSocket;

    /***************************************************************/

    public Client()
    {
        try {
            // Establish connection with Server via connection socket
            connectionSocket = new Socket("localhost", 21);

            CreateWritersReaders();
            // Send socket port number for data socket

        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
