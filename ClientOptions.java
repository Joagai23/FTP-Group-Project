import java.io.BufferedReader;
import java.io.IOException;

public class ClientOptions {

    public enum Options {

        LIST( "List all files on a directory" ),
        RETR( "Download a file from server" ),
        STOR( "Upload a file to the server" ),
        PWD( "Get the path to the working directory" ),
        CWD( "Change working directory" ),
        MKD( "Create directory" ),
        DELE( "Remove directory" ),
        RNFR( "Delete a file" ),
        QUIT( "Exit" );

        private final String description;

        Options( String description ) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    };

    /**
     * Loops the enumerator and prints a menu with the options
     */
    public static void printMenu() {

        System.out.println( "This are the options");

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
        System.out.println( "You have choose to: " + Options.values()[option].description );

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

        //System.out.println("func " + command);
        return command;
    }

}
