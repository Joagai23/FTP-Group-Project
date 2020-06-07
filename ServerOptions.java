import java.io.*;
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
	private static String directoryPath = "";
	private static final String MAIN_PATH = "C:\\Users\\Jorge\\Documents\\Universidad\\4\\RedesII\\Proyectos\\Server";
	private static String userName = "";
	private static String userPassword = "";
	private static boolean isUserLoggedIn = false;

	public enum Options {

		LIST( "List all files on a directory" ) {
			@Override
			public void doWork() {
				list();
			}
		},
		RETR( "Download a file from server" ) {
			@Override
			public void doWork() {
				download();
			}
		},
		STOR( "Upload a file to the server" ) {
			@Override
			public void doWork() {
				upload();
			}
		},
		PWD( "Get the path to the working directory" ) {
			@Override
			public void doWork() {
				getPath();
			}
		},
		CWD( "Change working directory" ) {
			@Override
			public void doWork() {
				changePath();
			}
		},
		MKD( "Create directory" ) {
			@Override
			public void doWork() {
				createDirectory();
			}
		},
		RMD( "Remove directory" ) {
			@Override
			public void doWork() {
				removeDirectory();
			}
		},
		DELE( "Delete a file" ) {
			@Override
			public void doWork() {
				deleteFile();
			}
		},
		RNFR( "Rename a file checking" ) {
			@Override
			public void doWork() {
				renameDirectory();
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
			}
		},
		QUIT( "Exit" ) {
			@Override
			public void doWork() {
				ServerOptions.sendCodeMessage(221);
				isUserLoggedIn = false;
			}
		},
		CLDT( "Close data connection" ) {
			@Override
			public void doWork() {
				server.closeServerDataSocket();
			}
		},
		USER( "Authentication user" ) {
			@Override
			public void doWork() {
				ServerOptions.sendCodeMessage(331);
			}
		},
		PASS( "Authentication password" ) {
			@Override
			public void doWork() {
				userControl(userName, userPassword);
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

	/******************************************************************************************************************************************/

	private static void list(){

		File folder = new File(MAIN_PATH + directoryPath);
		System.out.println("Directory: " + MAIN_PATH + directoryPath);
		if(!folder.exists())
		{
			sendCodeMessage(550);
		}
		else
		{
			String fileNames = "";

			if (folder.listFiles() != null) {
				for (final File fileEntry : folder.listFiles())
				{
					fileNames += " " + fileEntry.getName() + " ";
				}
			}

			if (fileNames.equals(""))
			{
				sendCodeMessage(450);
			}
			else
			{
				server.openDataSocket();
				sendCodeMessage(150);

				try {

					server.dataSocket = server.serverDataSocket.accept();
					System.out.println("Accepted data connection");

				}catch(IOException e){
					sendCodeMessage(425);
				}

				server.createDataWriters(true);

				server.getOutputCharacterDataSocket().println(fileNames);
				server.closeDataSocket();

				sendCodeMessage(226);
			}
		}
	}

	private static void download(){
		File currentDirectory = new File(directoryPath);

		FileInputStream fileInputStream;
		BufferedInputStream bufferedInputStream;

		int bufferSize = 1000;
		byte[] array = new byte[bufferSize];
		int n_bytes;

		try{
			System.out.println("Directory: " + MAIN_PATH + directoryPath);
			fileInputStream = new FileInputStream(MAIN_PATH + directoryPath);
			bufferedInputStream = new BufferedInputStream(fileInputStream);

			server.openDataSocket();
			sendCodeMessage(150);

			try {

				server.dataSocket = server.serverDataSocket.accept();
				System.out.println("Accepted data connection");

			}catch(IOException e){
				sendCodeMessage(425);
			}

			server.createDataWriters(false);

			while((n_bytes = bufferedInputStream.read(array, 0, bufferSize)) != -1){
				server.getOutputByteDataSocket().write(array, 0, n_bytes);
			}

			bufferedInputStream.close();
			server.closeDataSocket();
			System.out.println("Download complete!");
			directoryPath = currentDirectory.getParent();
			sendCodeMessage(226);

		}catch (FileNotFoundException e){
			sendCodeMessage(550);
		}catch (IOException e){
			sendCodeMessage(450);
		}
	}

	private static void upload(){

		File folder = new File(MAIN_PATH + directoryPath);
		System.out.println("Directory: " + MAIN_PATH + directoryPath);
		if(!folder.exists()) {
			sendCodeMessage(450);
		}else {
			server.openDataSocket();
			sendCodeMessage(150);

			try {
				server.dataSocket = server.serverDataSocket.accept();
				System.out.println("Accepted data connection");

			} catch (IOException e) {
				sendCodeMessage(425);
				e.printStackTrace();
			}

			server.createDataWriters(false);

			try {
				String fileName = server.getInputCommandSocket().readLine();

				System.out.println("File name: " + MAIN_PATH + directoryPath + "/" + fileName);

				File file = new File(MAIN_PATH + directoryPath + "/" + fileName);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

				byte[] array = new byte[1000];
				int n_bytes;

				while((n_bytes = server.getInputByteDataSocket().read(array)) != -1){
					bufferedOutputStream.write(array, 0, n_bytes);
				}

				bufferedOutputStream.close();

			} catch (FileNotFoundException e) {
				sendCodeMessage(553);
			} catch (IOException e) {
				e.printStackTrace();
				sendCodeMessage(451);
			}

			server.closeDataSocket();
			sendCodeMessage(226);
		}
	}

	private static void getPath(){

		try{
			String path = " Server" + directoryPath;

			if(directoryPath.isEmpty()){
				path += "\\";
			}

			server.getOutputCommandSocket().println(257 + path);
		}catch (NullPointerException e){
			System.out.println(e.toString());
		}

	}

	private static void changePath(){

		File folder = new File(MAIN_PATH + directoryPath);
		System.out.println("Directory: " + MAIN_PATH + directoryPath);
		if(!folder.exists())
		{
			directoryPath = "";
			sendCodeMessage(550);
		}else{
			sendCodeMessage(250);
		}
	}

	private static void createDirectory(){

		File folder = new File(MAIN_PATH + directoryPath);
		File parent = new File(folder.getParent());

		if(!parent.exists() || folder.exists()){
			directoryPath = "";
			sendCodeMessage(550);
		} else{
			folder.mkdir();
			server.getOutputCommandSocket().println(257 + " " + folder.toString());
		}
	}

	private static void removeDirectory(){

		File path = new File(MAIN_PATH + directoryPath);
		File currentDirectory = new File(directoryPath);

		if(!path.exists() || !path.isDirectory() || directoryPath.isEmpty() || directoryPath.contains(".")){
			directoryPath = "";
			sendCodeMessage(550);
		}else{
			directoryPath = currentDirectory.getParent();
			removeDirectoryFile(path);
		}
	}

	private static void deleteFile(){

		File path = new File(MAIN_PATH + directoryPath);
		File currentDirectory = new File(directoryPath);

		if(!path.exists() || !path.isFile()){
			directoryPath = "";
			sendCodeMessage(550);
		}else{
			directoryPath = currentDirectory.getParent();
			removeDirectoryFile(path);
		}
	}

	private static void renameDirectory(){

		File path = new File(MAIN_PATH + directoryPath);
		File currentDirectory = new File(directoryPath);

		if(!path.exists()|| directoryPath.isEmpty() || directoryPath.equals("/.") || directoryPath.equals("\\.")){
			directoryPath = "";
			sendCodeMessage(550);
		}else{

			sendCodeMessage(350);
			try {
				String[] directorySplit = directoryPath.split("\\.");
				String extension = "";
				if(directorySplit.length > 1){
					extension = directorySplit[directorySplit.length - 1];
				}

				String fileName = server.getInputCommandSocket().readLine();
				String[] parts = fileName.split(" |\\\\");
				fileName = parts[1] + "." + extension;

				String newPath = path.getAbsolutePath();
				newPath = newPath.replace(path.getName(),fileName);
				File renamedFile = new File(newPath);

				path.renameTo(renamedFile);

				sendCodeMessage(250);
				directoryPath = currentDirectory.getParent();
			} catch (IOException e) {
				sendCodeMessage(553);
			}
		}
	}

	/******************************************************************************************************************************************/

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

		try {
			parts = clientCommand.split(" |\\\\");
			option = parts[0];

			if (parts.length > 1) {
				if (!parts[0].equals("PURT") && !parts[1].equals("r") && !parts[0].equals("USER") && !parts[0].equals("PASS")) {
					setDirectory(parts, 1);
				}
			}

			if (option.compareTo("PURT") == 0) {
				setDataPort(parts[1]);
			} else if (option.compareTo("USER") == 0) {
				userName = parts[1];
				System.out.println("User name: " + userName);
			} else if (option.compareTo("PASS") == 0) {
				userPassword = parts[1];
				System.out.println("User password: " + userPassword);
			}


		}catch(NullPointerException e){
			System.out.println(e.toString());
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

	private static void setDirectory(String[] partArray, int startingIndex){

		directoryPath = "\\";

		for(int i = startingIndex; i < partArray.length; ++i){
			if(partArray[i].equals("r")){
				i = partArray.length;
			}else{
				if(i != startingIndex && !partArray[i].isEmpty()){
					directoryPath += "/";
				}
				directoryPath += partArray[i];
			}
		}
	}

	private static void  removeDirectoryFile(File directory)  {
		File[] files = directory.listFiles();
		if(files!=null)
		{
			for(File f: files) {
				if(f.isDirectory()) {
					removeDirectoryFile(f);
				} else {
					f.delete();
				}
			}
		}
		directory.delete();
		sendCodeMessage(250);
	}

	public static void userControl(String user,String password)
	{
		boolean dataLoginFound = false;

		try {
			BufferedReader reader = new BufferedReader(new FileReader("userControl.txt"));
			String line = reader.readLine();

			while (line != null && dataLoginFound == false) {

				//Inside the .txt we have the user and the password in the same line, divided by two scrpts(--)
				//For that,we use split() for divides into two parts the line
				//In this way we have an array with two elements,the first will be the user and the second one will be the password

				String[] datasLogin = line.split("--");

				if (datasLogin[0].equals(user) && datasLogin[1].equals(password))
					dataLoginFound = true;
				else
					line = reader.readLine();

			}
		}catch(FileNotFoundException fNFE){
			ServerOptions.sendCodeMessage(530);
		}
		catch(IOException iOE){
			ServerOptions.sendCodeMessage(530);
		}

		if(dataLoginFound){
			System.out.println("Welcome "+ user);
			isUserLoggedIn = true;
			ServerOptions.sendCodeMessage(230);
		}else{
			System.out.println("You are not registered "+ user);
			ServerOptions.sendCodeMessage(530);
		}
	}

	public static boolean getLogIn(){
		return isUserLoggedIn;
	}

	public static String getUserName(){
		return userName;
	}
}
