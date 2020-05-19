import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private int commandPort = 0;
    private final int dataPort = 22;
    private String configFile = "config.txt";
    private final String IP = "localhost";
    private Socket commandSocket;
    private Socket dataSocket;
    private BufferedReader inputConnectionSocket;
    private PrintWriter outputConnectionSocket;
    private BufferedReader inputCharDataSocket;
    private DataInputStream inputByteDataSocket;
    private DataOutputStream outputByteDataSocket;

    /***************************************************************/

    public Client()
    {
        setPortFromConfigFile();
        establishConnection();
    }

    /***************************************************************/

    public Socket getCommandSocket()
    {
        return commandSocket;
    }

    public BufferedReader getInputConnectionSocket()
    {
        return inputConnectionSocket;
    }

    public PrintWriter getOutputConnectionSocket()
    {
        return outputConnectionSocket;
    }

    public DataInputStream getInputByteDataSocket(){
        return inputByteDataSocket;
    }

    public DataOutputStream getOutputByteDataSocket() { return  outputByteDataSocket; }

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

    public void createDataReader(int mode){

        try {

            if(mode == 1){ // character mode
                inputCharDataSocket = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
            }else{ // byte mode
                inputByteDataSocket = new DataInputStream(dataSocket.getInputStream());
                outputByteDataSocket = new DataOutputStream(dataSocket.getOutputStream());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPortFromConfigFile() {

        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(configFile));
            try {
                commandPort = Integer.parseInt(fileReader.readLine());
                System.out.println("Command port set to: " + commandPort);
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
            createCommandWritersReaders();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void openDataSocket() {

        try {
            dataSocket = new Socket(IP, dataPort);
            //dataSocket.setSoTimeout(2000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Data connection open!");
    }

    public void closeDataSocket(){

        try {
            if(!dataSocket.isClosed()){
                dataSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Data connection closed!");
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

    private String getCommandPortConnection(int port) {

        String command = "";
        String hostport = getLocalhost();
        String p1 = "";
        String p2 = "";
        String binaryPort = Integer.toBinaryString(0x10000 | port).substring(1);

        p1 = Integer.toString(Integer.parseInt(binaryPort.substring(0, 8), 2));
        p2 = Integer.toString(Integer.parseInt(binaryPort.substring(8, 16), 2));

        command = command.concat("PURT").concat(" ").concat(hostport).concat(".").concat(p1).concat(".").concat(p2).concat("\\r\\n");

        command = command.replace(".", ",");

        System.out.println(command);

        return command;
    }

    public String sendPort() {
        return getCommandPortConnection(dataPort);
    }

    public String readData(int mode){
        String inputFromServer = "default";
        try {
            if (mode == 1) {
                inputFromServer = inputCharDataSocket.readLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return inputFromServer;
    }
}
