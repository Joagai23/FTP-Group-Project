import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerOptions {

	private static Server server;

	public ServerOptions(Server server)
	{
		this.server = server;
	}

	private static String logFile = "serverLog.txt";
	private static int dataPortNumber = 0;

	public enum Options {

		LIST( "List all files on a directory" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
				list();
				//listFiles("test", server.getOutputCommandSocket());
			}
		},
		RETR( "Download a file from server" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
				sendCodeMessage(200);
			}
		},
		STOR( "Upload a file to the server" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
				sendCodeMessage(200);
			}
		},
		PWD( "Get the path to the working directory" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
				sendCodeMessage(200);
			}
		},
		CWD( "Change working directory" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
				sendCodeMessage(200);
			}
		},
		MKD( "Create directory" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
				sendCodeMessage(200);
			}
		},
		RMD( "Remove directory" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
				sendCodeMessage(200);
			}
		},
		DELE( "Delete a file" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
				sendCodeMessage(200);
			}
		},
		RNFR( "Rename a file" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
				sendCodeMessage(200);
			}
		},
		PURT( "Port to establish data connection" ) {
			@Override
			public void doWork() {

				if(server.setDataPort(dataPortNumber)){
					sendCodeMessage(200);
				}else{
					sendCodeMessage(503);
				}

				System.out.println(this.name());
			}
		},
		QUIT( "Exit" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
				ServerOptions.sendCodeMessage(221);
			}
		},
		CLDT( "Close data connection" ) {
			@Override
			public void doWork() {
				System.out.println(this.name());
				server.closeDataSocket();
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
	}

	/**
	 * Receives an option an loops between all the options doing the function that corresponds to it
	 * @param option
	 */
	public static void readOption(String option) {

		for ( Options options : Options.values() ) {
			if ( options.name().equals(option) ) {
				options.doWork();
			}
		}

	}//readOption

	
	/**
	 * Receives an Integer that will be the code and print the code to the client
	 * @param code
	 */
	public static void sendCodeMessage(int code) {

		switch( code ) {
			case 150:
				server.getOutputCommandSocket().println( code + ": File status OK; about to open data connection.");
				break;
			case 200:
				server.getOutputCommandSocket().println( code + ": Command okey.");
				break;
			case 202:
				server.getOutputCommandSocket().println( code + ": Command not implemented, superfluous at this site.");
				break;
			case 220:
				server.getOutputCommandSocket().println( code + ": Service ready for new user.");
				break;
			case 221:
				server.getOutputCommandSocket().println( code + ": Service closing control conection");
				break;
			case 226:
				server.getOutputCommandSocket().println( code + ": Closing data connection. Requested file action successful.");
				break;
			case 230:
				server.getOutputCommandSocket().println( code + ": User logged in, proceed.");
				break;
			case 250:
				server.getOutputCommandSocket().println( code + ": Requested file action OK, completed.");
				break;
			case 257:
                server.getOutputCommandSocket().println( code + ": Code 257");
				break;
			case 331:
				server.getOutputCommandSocket().println( code + ": User name OK, need password.");
				break;
			case 350:
				server.getOutputCommandSocket().println( code + ": Requested file action pending futher information.");
				break;
			case 421:
				server.getOutputCommandSocket().println( code + ": Service not available, closing control connection.");
				break;
			case 453:
				server.getOutputCommandSocket().println( code + ": Requested action not taken. File name not allowed.");
				break;
			case 425:
				server.getOutputCommandSocket().println( code + ": Can't open data connection.");
				break;
			case 426:
				server.getOutputCommandSocket().println( code + ": Connection closed; transfer aborted.");
				break;
			case 450:
				server.getOutputCommandSocket().println( code + ": Requested file action not taken. File unavailable.");
				break;
			case 451:
				server.getOutputCommandSocket().println( code + ": Requested action aborted: local error in processing.");
				break;
			case 452:
				server.getOutputCommandSocket().println( code + ": Requested action not taken. Insufficient storage space in system.");
				break;
			case 500:
				server.getOutputCommandSocket().println( code + ": Syntax error, command unrecognized.");
				break;
			case 501:
				server.getOutputCommandSocket().println( code + ": Syntax error in parameters or arguments.");
				break;
			case 502:
				server.getOutputCommandSocket().println( code + ": Command not implemented.");
				break;
			case 503:
				server.getOutputCommandSocket().println( code + ": Bad sequence of commands.");
				break;
			case 530:
				server.getOutputCommandSocket().println( code + ": Not logged in.");
				break;
			case 550:
				server.getOutputCommandSocket().println( code + ": Requested action not taken. File unavailable.");
				break;
			case 553:
				server.getOutputCommandSocket().println( code + ": Requested action not taken. File name not allowed.");
				break;
			default:
				server.getOutputCommandSocket().println( "ERROR, unknown code: " + code );
				break;
		}
	}

	public static String getPathCurrentDirectory() {

		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString();
		return path;
	}

	public String getDirectoryName() {
		return "directoy";
	}

	public static void list(){

		//Try to get file
		//if file is busy at the moment
			//send client code 450 via command socket
		//if file cant be found or server doesn't have access
			//send client code 550 via command socket
		//else everything is fine
			//open data socket and send client code 150 via command socket
			server.openDataSocket();
			sendCodeMessage(150);

			try {

				server.dataSocket = server.serverDataSocket.accept();
				System.out.println("Accepted data connection");

			}catch(IOException e){
				//send error 425
				e.printStackTrace();
			}

			server.createDataWriters(true);

			//if any problem
				//send 451
			//else
				//send file via data socket
				server.getOutputCharacterDataSocket().println("LIST OF FILES");
				//send code 226
				sendCodeMessage(226);
	}

	public static void listFiles(String listPath, PrintWriter output) {
		//First, Check that client is listening the port in character mode
		//Second, Check if they send directory if not, we assume is current folder
		//Third, Send messages and list the files
		String path;


		/***** SECOND STEP *****/
		if( listPath.equals("") || listPath.isEmpty() || listPath == null ) {
			path = getPathCurrentDirectory();
		}
		else {
			path = listPath;
		}

		sendCodeMessage(150);
			sendCodeMessage(226);
			sendCodeMessage(425);
			sendCodeMessage(451);
		sendCodeMessage(450);
		sendCodeMessage(550);

	}

	public static void registerAction(String user, String command) {

		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

		String formattedDate = date.format(dateFormat);

		try {
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(logFile, true));

			fileWriter.newLine();
			fileWriter.write("User: " + user + " - " + formattedDate + " - " + command);

			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * It receives the full command send by client and returns just the option selected
	 *
	 * @param clientCommand
	 * @return
	 */
	public static String getOption(String clientCommand) {
		String option = "";
		String[] parts;

		parts = clientCommand.split(" |\\\\");
		option = parts[0];

		if(option.compareTo("PURT") == 0){

			setDataPort(parts[1]);
		}

		return option;
	}

	public static void setDataPort(String portMessage){

		//Split the numbers in the portMessage, 0-3 (IP) & 4-5 (PORT)
		String[] splitString = portMessage.split(",");

		int high = Integer.parseInt(splitString[4]);
		int low = Integer.parseInt(splitString[5]);

		int port = high * 256 + low;
		dataPortNumber = port;
	}
}
