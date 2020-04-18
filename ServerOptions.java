import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerOptions {

	private static Server server;

	public ServerOptions(Server server)
	{
		this.server = server;
	}

	public enum Options {

	LIST( "List all files on a directory" ) {
		@Override
		public void doWork() {
			System.out.println(this.name());
			listFiles("test", server.getOutputConnectionSocket());
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
	PORT( "Port to establish data connection" ) {
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

	public static void sendCodeMessage(int code /*,int code257*/, PrintWriter output ) {

		switch( code ) {
			case 150:
				output.println( code + ": File status OK; about to open data connection.");
				break;
			case 200:
				output.println( code + ": Command okey.");
				break;
			case 202:
				output.println( code + ": Command not implemented, superfluous at this site.");
				break;
			case 220:
				output.println( code + ": Service ready for new user.");
				break;
			case 221:
				output.println( code + ": Service closing control conection");
				break;
			case 226:
				output.println( code + ": Closing data connection. Requested file action successful.");
				break;
			case 230:
				output.println( code + ": User logged in, proceed.");
				break;
			case 250:
				output.println( code + ": Requested file action OK, completed.");
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
				output.println( code + ": User name OK, need password.");
				break;
			case 350:
				output.println( code + ": Requested file action pending futher information.");
				break;
			case 421:
				output.println( code + ": Service not available, closing control connection.");
				break;
			case 453:
				output.println( code + ": Requested action not taken. File name not allowed.");
				break;
			case 425:
				output.println( code + ": Can't open data connection.");
				break;
			case 426:
				output.println( code + ": Connection closed; transfer aborted.");
				break;
			case 450:
				output.println( code + ": Requested file action not taken. File unavailable.");
				break;
			case 451:
				output.println( code + ": Requested action aborted: local error in processing.");
				break;
			case 452:
				output.println( code + ": Requested action not taken. Insufficient storage space in system.");
				break;
			case 500:
				output.println( code + ": Syntax error, command unrecognized.");
				break;
			case 501:
				output.println( code + ": Syntax error in parameters or arguments.");
				break;
			case 502:
				output.println( code + ": Command not implemented.");
				break;
			case 503:
				output.println( code + ": Bad sequence of commands.");
				break;
			case 530:
				output.println( code + ": Not logged in.");
				break;
			case 550:
				output.println( code + ": Requested action not taken. File unavailable.");
				break;
			case 553:
				output.println( code + ": Requested action not taken. File name not allowed.");
				break;
			default:
				output.println( "ERROR, unknown code: " + code );
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

		sendCodeMessage(150, output);
		sendCodeMessage(226, output);
		sendCodeMessage(425, output);
		sendCodeMessage(451, output);
		sendCodeMessage(450, output);
		sendCodeMessage(550, output);

	}
}
