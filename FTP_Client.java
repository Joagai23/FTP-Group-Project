import java.io.*;

public class FTP_Client {

	static BufferedReader inputKeyboard;

    public static void main(String args[]) {

		Client client = new Client();
		ClientOptions clientOptions = new ClientOptions();

        String result;
		String option;
		String command;
		String path;
		boolean connection = true;

        try {

			String dataPortResponse = client.getInputConnectionSocket().readLine();
			System.out.println(dataPortResponse);
			if(dataPortResponse.compareTo("OK") != 0)
			{
				connection = false;
			}

			while(connection)
            {
                // Get text from the keyboard
                inputKeyboard = new BufferedReader(new InputStreamReader(System.in));

				clientOptions.printMenu();
                option = clientOptions.getOption(inputKeyboard);
				path = clientOptions.getPath(inputKeyboard);
				command = clientOptions.getCommand(option, path);

                // Send data to the server
                client.getOutputConnectionSocket().println(command);
                // Read data from the server
                result = client.getInputConnectionSocket().readLine();

                System.out.println("Server says: " + result);

                if(result.compareTo("QUIT") == 0)
                {
                    connection = false;
                }
            }

            // Close the connection
			client.getCommandSocket().close();
        }  catch(IOException e) {
            System.out.println("Error: " + e);
        }
    } // main
} // class CharacterClient
