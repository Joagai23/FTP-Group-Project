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

    public static void sendCodeMessage(int code /*,int code257*/) {

        switch( code ) {
            case 150:
                System.out.println( code + ": File status OK; about to open data connection.");
                break;
            case 200:
                System.out.println( code + ": Command okey.");
                break;
            case 202:
                System.out.println( code + ": Command not implemented, superfluous at this site.");
                break;
            case 220:
                System.out.println( code + ": Service ready for new user.");
                break;
            case 221:
                System.out.println( code + ": Service closing control conection");
                break;
            case 226:
                System.out.println( code + ": Closing data connection. Requested file action successful.");
                break;
            case 230:
                System.out.println( code + ": User logged in, proceed.");
                break;
            case 250:
                System.out.println( code + ": Requested file action OK, completed.");
                break;
            case 257:
            /*
            switch( code257 ) {
            case 1:
                String path = getPathWorkingDirectory();
                System.out.println( code + ": " + path);
                break;
            case 2:
                String directory = getDirectoryName();
                System.out.println( code + ": " + directory + " created.");
                break;
            case 3:
                System.out.println( code + ": Entering Passive Mode (h1, h2, h3, h4, p1, p2)");
                break;
            default:
                System.out.println( code );
                break;
            }
            */
                break;
            case 331:
                System.out.println( code + ": User name OK, need password.");
                break;
            case 350:
                System.out.println( code + ": Requested file action pending futher information.");
                break;
            case 421:
                System.out.println( code + ": Service not available, closing control connection.");
                break;
            case 453:
                System.out.println( code + ": Requested action not taken. File name not allowed.");
                break;
            case 425:
                System.out.println( code + ": Can't open data connection.");
                break;
            case 426:
                System.out.println( code + ": Connection closed; transfer aborted.");
                break;
            case 450:
                System.out.println( code + ": Requested file action not taken. File unavailable.");
                break;
            case 451:
                System.out.println( code + ": Requested action aborted: local error in processing.");
                break;
            case 452:
                System.out.println( code + ": Requested action not taken. Insufficient storage space in system.");
                break;
            case 500:
                System.out.println( code + ": Syntax error, command unrecognized.");
                break;
            case 501:
                System.out.println( code + ": Syntax error in parameters or arguments.");
                break;
            case 502:
                System.out.println( code + ": Command not implemented.");
                break;
            case 503:
                System.out.println( code + ": Bad sequence of commands.");
                break;
            case 530:
                System.out.println( code + ": Not logged in.");
                break;
            case 550:
                System.out.println( code + ": Requested action not taken. File unavailable.");
                break;
            case 553:
                System.out.println( code + ": Requested action not taken. File name not allowed.");
                break;
            default:
                System.out.println( "ERROR, unknown code: " + code );
                break;
        }
    }

    public String getPathWorkingDirectory() {
        return "path";
    }

    public String getDirectoryName() {
        return "directoy";
    }
}
