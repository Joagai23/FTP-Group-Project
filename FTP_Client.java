import java.io.*;

public class FTP_Client {

	static BufferedReader inputKeyboard;

    public static void main(String args[]) {

		Client client = new Client();

        String result;
		String option;
		String path = "";
		boolean connection = true;

        try {

			String dataPortResponse = client.getInputConnectionSocket().readLine();
			System.out.println(dataPortResponse);
			if(!dataPortResponse.contains("220"))
			{
				connection = false;
			}

			while(connection)
            {
                // Get text from the keyboard
                inputKeyboard = new BufferedReader(new InputStreamReader(System.in));

                ClientOptions.printMenu();
                option = ClientOptions.getOption(inputKeyboard);

                if (ClientOptions.needPath(option)) {
                    path = ClientOptions.getPath(inputKeyboard);
                }

                result = ClientOptions.FTPClient(client, option, path);
                System.out.println("Server says: " + result);

                if(result.contains("221"))
                {
                    connection = false;
                }
            }

            // Close the connection
			client.getCommandSocket().close();
			System.out.println("Client connection closed!");
        }  catch(IOException e) {
            System.out.println("Error: " + e);
        }
    } // main
} // class CharacterClient
