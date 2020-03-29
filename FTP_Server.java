import java.net.*;

import java.io.*;

public class FTP_Server {
	
public enum Options {
		
		LIST( "List all files on a directory" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
		RETR( "Download a file from server" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
		STOR( "Upload a file to the server" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
		PWD( "Get the path to the working directory" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
		CWD( "Change working directory" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
		MKD( "Create directory" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
		DELE( "Remove directory" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		}, 
		RNFR( "Delete a file" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		},
		QUIT( "Exit" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
			}
		};
		
		private final String description;
		
		Options( String description ) {
			this.description = description;
		}
		
		public String getDescription() {
			return description;
		}
		
		public abstract void doWork();
	};

    public static void main(String args[]) {

        Server server = new Server();
        boolean serverControl = true;
        boolean connectionControl = false;
        BufferedReader input;
        PrintWriter output;

        String data = "";

        while(true)
        {
            try
            {
                System.out.println("Character Server waiting for requests");
                // Accept a connection  and create the socket for the transmission with the client
                server.connectionSocket = server.serverConnectionSocket.accept();
                connectionControl = true;
                System.out.println("Connection accepted");

                while(connectionControl)
                {
                    // Get the input/output from the socket
                    input = new BufferedReader(new InputStreamReader(server.connectionSocket.getInputStream()));
                    output = new PrintWriter(server.connectionSocket.getOutputStream(), true);

                    // Read the data sent by the client
                    data =  input.readLine();
                    System.out.println("Server receives: " + data);
                    // Convert text to Upper Case
                    //data = data.toUpperCase();
                    // Send the text
                    //data = "220 Service ready for new user";
                    readOption(data);
                    output.println(data);
                    
                    System.out.println("Server sends: " + data);

                    if(data.compareTo("QUIT") == 0)
                    {
                        connectionControl = false;
                    }
                }

                // Close the socket
                server.connectionSocket.close();

            } catch(IOException e)
            {
                System.out.println("Error: " + e);
            }
        }
        // Close the server socket
        //server.getServerConnectionSocket().close();
    } // main
    
    
    /**
     * Receives an option an loops between all the options doing the function that corresponds to it
     * @param option
     */
    public static void readOption(String option) {
    	
    	for ( Options options : Options.values() ) {
    		if ( options.name().compareTo(option) == 0 ) {
    			options.doWork();
    		}
    	}
 	
    }//readOption
} // class CharacterServer
