import java.io.*;
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
    private PrintWriter outputCharacterDataSocket;
    private DataOutputStream outputByteDataSocket;
    private DataInputStream inputByteDataSocket;

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

    public boolean setDataPort(int portNumber)
    {
        if(portNumber > 0 && portNumber < 65535)
        {
            dataPort = portNumber;
            System.out.println("Data port number set to: " + portNumber);
            return true;
        }
        else
        {
            System.out.println("Data port number is not available!");
            return false;
        }
    }

    public BufferedReader getInputCommandSocket()
    {
        return inputCommandSocket;
    }

    public PrintWriter getOutputCommandSocket()
    {
        return outputCommandSocket;
    }

    public PrintWriter getOutputCharacterDataSocket(){ return outputCharacterDataSocket;}

    public DataOutputStream getOutputByteDataSocket() { return outputByteDataSocket;}

    public DataInputStream getInputByteDataSocket() { return inputByteDataSocket; }

    /***************************************************************/

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

    public void createDataWriters(boolean characterMode){

        try{
            if(characterMode){
                outputCharacterDataSocket = new PrintWriter(dataSocket.getOutputStream(), true);
            }else{
                outputByteDataSocket = new DataOutputStream(dataSocket.getOutputStream());
                inputByteDataSocket = new DataInputStream(dataSocket.getInputStream());
            }
        }catch (IOException e)
        {
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

    public void closeServerDataSocket()
    {
        try {
            serverDataSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Data connection closed");
    }

    public void closeDataSocket(){
        try {
            dataSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
