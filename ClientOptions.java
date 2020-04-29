import java.io.BufferedReader;
import java.io.IOException;

public class ClientOptions {

    public enum Options {

        LIST( "List all files on a directory", true ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
        RETR( "Download a file from server", true ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
        STOR( "Upload a file to the server", true ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
        PWD( "Get the path to the working directory", false ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
        CWD( "Change working directory", true ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
        MKD( "Create directory", true ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
        RMD( "Remove directory", true ){
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
        DELE( "Remove file", true ){
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
        RNFR( "Rename a file", true ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
        QUIT( "Exit", false ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		};

        private final String description;
        private final boolean needPath;

        Options( String description, boolean needPath ) {
            this.description = description;
            this.needPath = needPath;
        }

        public String getDescription() {
            return description;
        }
        
        public abstract void doWork();
    };

    /**
     * Loops the enumerator and prints a menu with the options
     */
    public static void printMenu() {

        System.out.println( "These are the options");

        for ( Options options : Options.values() ) {
            System.out.println( "\t(" + options.ordinal() + ") " + options.description );
        }
    } //printMenu

    /**
     * Ask for an option, checks if it is a valid one, and returns the name of the enumerator
     *
     * @return command
     */
    public static String getOption(BufferedReader inputKeyboard) {

        int option = Options.values().length; //This will avoid get an option in case the inputConnectionSocket is a string
        String command = "";

        do {
            System.out.println( "Which option do you want to select: " );
            try {
                option = Integer.parseInt(inputKeyboard.readLine());
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if( option < 0 || option > Options.values().length - 1) {
                System.out.println( "That option is not implemented." );
            }
        }while(option < 0 || option > Options.values().length - 1);

        command = Options.values()[option].name();
        System.out.println( "You have chosen to: " + Options.values()[option].description );

        return command;
    }

    /**
     * Ask the user for a path
     *
     * @param inputKeyboard
     * @return path
     */
    public static String getPath(BufferedReader inputKeyboard) {

        String path = "";

        System.out.println( "Introduce a path: " );
        try {
            path = inputKeyboard.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return path;
    }

    /**
     * It will return the complete command that we will send to the server
     *
     * @param option 	//It will receive the Option string, ex. LIST.
     * @param path 		//Path that is required for some commands
     * @return command
     */
    public static String getCommand(String option, String path) {

        String command = "";
        String SP = " ";
        String CRLF = "\\r\\n";

        command = option;

        if( path.isEmpty() || path == null ) {
            command = command.concat(CRLF);
        }
        else {
            command = command.concat(SP).concat(path).concat(CRLF);
        }

        return command;
    }
    
    
    /**
     * Checks if the option needs a path to complete the command
     * 
     * @param option
     * @return needPath
     */
    public static boolean needPath(String option) {
    	
    	boolean needPath = false;
    	
    	for ( Options options : Options.values() ) {
            if( options.name().equals(option)) {
            	needPath = options.needPath;
            }
        }
    	return needPath;
    }

    // Returns 0 if command doesn't require data, 1 if requires character mode data connection and 2 if byte data connection
    public static int requiresDataConnection(String option){

        if(option.compareTo("LIST") == 0){
            return  1;
        }else if(option.compareTo("RETR") == 0 || option.compareTo("STOR") == 0){
            return 2;
        }

        return 0;
    }

    public static String FTPClient(Client client, String option, String path) {

        try{

            // Check if command requires data connection and send PORT command if so
            int requiresDataConnection = requiresDataConnection(option);
            if(requiresDataConnection != 0) {
                String portCommand = client.sendPort();

                // Send port command and wait for response from server
                client.getOutputConnectionSocket().println(portCommand);
                String portResponse = client.getInputConnectionSocket().readLine();

                System.out.println("Response from port: " + portResponse);

                if (portResponse.contains("503")) { // Bad sequence of commands.
                    return portResponse;
                }
            }

            // Create command to send to server
            String command = getCommand(option, path);
            System.out.println(command);

            // Send data to the server
            client.getOutputConnectionSocket().println(command);
            // Read data from the server
            String response = client.getInputConnectionSocket().readLine();

            if(!response.contains("150")){ // Doesn't need to open data connection
                return response;
            }

            // Open data connection
            client.openDataSocket();
            client.createDataReader(requiresDataConnection);

            // Wait for next command and close connection
            /*do{

                System.out.println("C: " + response);
                System.out.println("D: " + data);
            }while(!response.contains("226") || !response.contains("451") || !response.contains("425") || !response.contains("426"));*/

            String data = client.readData(requiresDataConnection);
            response = client.getInputConnectionSocket().readLine();
            System.out.println("D: " + data);

            client.closeDataSocket();
            client.getOutputConnectionSocket().println("CLDT");
            return response;

        }catch (IOException e) {
            e.printStackTrace();
        }

        return "QUIT";
    }
}
