import java.net.*;
import java.io.*;

public class FTP_Client {

    public static void main(String args[]) {
        int commandPort = 21;
        Socket sCon = null;
        BufferedReader input;
        PrintWriter output;

        BufferedReader inputKeyboard;
        String data;
        String result;
        boolean connection = true;

        try {
            // Connect to the server
            sCon = new Socket("localhost", commandPort);

            while(connection)
            {
                // Get the input/output from the socket
                input = new BufferedReader(new InputStreamReader(sCon.getInputStream()));
                output = new PrintWriter(sCon.getOutputStream(), true);

                // Get text from the keyboard
                inputKeyboard = new BufferedReader(new InputStreamReader(System.in));
                //System.out.print("Write text (END to close the server): ");
                data = inputKeyboard.readLine();

                // Send data to the server
                output.println(data);
                // Read data from the server
                result = input.readLine();

                //System.out.println("Data = " + data + " --- Result = " + result);
                System.out.println("Server says: " + result);

                if(result.compareTo("END") == 0)
                {
                    connection = false;
                }
            }

            // Close the connection
            sCon.close();
        }  catch(IOException e) {
            System.out.println("Error: " + e);
        }
    } // main
} // class CharacterClient