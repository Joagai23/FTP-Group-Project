import java.net.*;
import java.util.Scanner;
import java.io.*;

public class FTP_Client {
	
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

    public static void main(String args[]) {
        int commandPort = 21;
        Socket sCon = null;
        BufferedReader input;
        PrintWriter output;

        BufferedReader inputKeyboard;
        String data;
        String result;
        boolean connection = true;
        
        String option;
        

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
                
                printMenu();
                option = getOption(inputKeyboard);
                
                
                //data = inputKeyboard.readLine();

                // Send data to the server
                //output.println(data);
                output.println(option);
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
    
    /**
     * Loops the enumerator and prints a menu with the options
     */
    private static void printMenu() {
    	
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
    private static String getOption(BufferedReader inputKeyboard) {
    	
    	int option = Options.values().length; //This will avoid get an option in case the input is a string
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
    
    
    
} // class CharacterClient
