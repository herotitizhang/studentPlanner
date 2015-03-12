package backendIO;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.GregorianCalendar;

import backendIO.ClientRequest.RequestType;
import model.ScheduleI;


// Note: processing serverResponse should be separate from the functionality provided in this class
public class ServerCommunicator {

	
	// if loggedIn is true, the user can not send LOGIN or CREATE request
	// if loggedIn is false, the user can not send
	private static boolean loggedIn = false;
	
	private static String username = null;
	private static String password = null;
	
	// if not authenticated, then the phone number is not used by the server to send alerts
	private static boolean authenticated = false; //TODO check where it's used. maybe get rid of it?
	private static String phoneNumber = null;
	
	private static String serverIP;
	private static int port = 12345;
	
	static {
		try {
			BufferedReader br = new BufferedReader(new FileReader("serverIP.txt"));
			serverIP = br.readLine().trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

	/**
	 * false IP will make it hang...
	 * @return
	 */
	public static boolean checkConnection() {
		
		// a wrapper class so that connection variable can be modified in an anonymous class
		class ImmutableBoolean {
			boolean connected = false;
		}
		ImmutableBoolean internentConnection = new ImmutableBoolean();
		
		// a runnable object that checks 
		Thread connectionThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// if no server is running on this ip, the following line will hang
					// and the connected variable will always be false;
					Socket socket = new Socket(serverIP, port); 
					internentConnection.connected = true;
				} catch (IOException e) {
					System.out.println("No internet connection."); // connected variable is unchanged; it is false
				} 
			}
			
		});
		
		// wait for 3 seconds. if there is no response from server, the variable will be false. 
		try {
			Thread.sleep(2*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return internentConnection.connected;
		
	}
	
	/**
	 * the invoker will call this method like ServerResponse sr = ServerCommunicator.sendClientRequest 
	 * the invoker only proceeds after ServerResponse is assigned a value; 
	 * throw the ConnectionException (which extends IOException) and let the invoker handles it
	 * 
	 * Note: the ConnectionException (IOException) indicates that internet is not available,
	 * in this case the invoker should switch to local save/load.
	 * 
	 * throw the unknownhostException (should not happen as long as the serverIP
	 * and port are correct)
	 * 
	 * the invoker also needs to check for null due to unexpected disconnection
	 */
	public static ServerResponse sendClientRequest(ClientRequest clientRequest) throws IOException{

		// initialization
		ServerResponse serverResponse = null;
		Socket socket = null;
		try {
			socket = new Socket(serverIP, port);
		} catch (UnknownHostException e) {
			System.out.println("unknown host"); // should not happen
		} 
		
		try {
			// send ClientRequest to the server
			socket.getOutputStream().write(ClientIOSystem.getByteArray(clientRequest));
			socket.getOutputStream().flush();

			// start the timer so that it won't wait for the server's response forever
			Timer timer = new Timer();
			new Thread(timer).start();

			// get Server Response from the server
			while ((!socket.isClosed()) && socket.isConnected() && (!timer.timeOut)) {
				if (socket.getInputStream().available() > 0) {
					InputStream in = socket.getInputStream();
					byte[] content = new byte[in.available()]; 
					in.read(content);
					serverResponse = (ServerResponse) ClientIOSystem.getObject(content);
					socket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// if it's a create or a login request, and the server's response is positive,
		// then set the login state to true
		if ((clientRequest.getType() == RequestType.CREATE || clientRequest.getType() == RequestType.LOGIN)
				 && (serverResponse!= null) && (serverResponse.isAccepted())) {
			loggedIn = true;
			username = clientRequest.getUserName();
			password = decryptPassword(clientRequest.getPassword());
		}
		 
		return serverResponse;
	 
	}

	public static ClientRequest generateCreateRequest(String username, String password) {
		ClientRequest request = null;
		if (!loggedIn) {
			request = new ClientRequest(RequestType.CREATE);
			request.setUserName(username);
			request.setPassword(encryptPassword(password));
		}
		return request;
	}
	
	public static ClientRequest generateLoginRequest(String username, String password) {
		ClientRequest request = null;
		if (!loggedIn) {
			request = new ClientRequest(RequestType.LOGIN);
			request.setUserName(username);
			request.setPassword(encryptPassword(password));
		}
		return request;
	}

	public static ClientRequest generateSaveRequest(ScheduleI schedule) {
		ClientRequest request = new ClientRequest(RequestType.SAVE);
		request.setUserName(username);
		request.setPassword(encryptPassword(password)); // not necessary but it's good to include
		request.setSchedule(ClientIOSystem.getByteArray(schedule));
		return request;
	}
	
	public static ClientRequest generateLoadRequest() {
		ClientRequest request = new ClientRequest(RequestType.LOAD);
		request.setUserName(username);
		request.setPassword(encryptPassword(password)); // not necessary but it's good to include
		return request;
	}
	
	public static ClientRequest generateRequestAuthRequest(String phoneNumber) { // TODO support email 
		ClientRequest request = new ClientRequest(RequestType.REQUEST_AUTH);
		request.setUserName(username);
		request.setPassword(encryptPassword(password)); // not necessary but it's good to include
		request.setPhoneNumber(phoneNumber);
		return request;
	}
	
	public static ClientRequest generateAuthenticateRequest(String authenCode) {
		ClientRequest request = new ClientRequest(RequestType.AUTH);
		request.setUserName(username);
		request.setPassword(encryptPassword(password)); // not necessary but it's good to include
		request.setAuthenCode(authenCode);
		return request;
	}
	
	
	/*
	 * 
	 * String eventName = null;
	String alertText = null;
	GregorianCalendar alertTime = null;
	String repeat = null;
	 */
	public static ClientRequest generateAlertRequest(String title, String alertText, String repeat, GregorianCalendar alertTime) {
		ClientRequest request = new ClientRequest(RequestType.ALERT);
		request.setUserName(username);
		request.setPassword(encryptPassword(password)); // not necessary but it's good to include
		request.setAlertTitle(title);
		request.setAlertText(alertText);
		request.setAlertTime(alertTime);
		request.setRepeat(repeat);
		return request;
	}
	
	public static String encryptPassword(String password) {
		String encryptedPassword = "";
		for (int i = 0; i < password.length(); i++) {
			encryptedPassword += (char)(password.charAt(i)+1);
		}
		return encryptedPassword;
	}
	
	public static String decryptPassword(String password) {
		String decryptedPassword = "";
		for (int i = 0; i < password.length(); i++) {
			decryptedPassword += (char)(password.charAt(i)-1);
		}
		return decryptedPassword;
	}

	public static boolean isLoggedIn() {
		return loggedIn;
	}

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}

	public static String getServerIP() {
		return serverIP;
	}

	public static int getPort() {
		return port;
	}

	public static boolean isAuthenticated() {
		return authenticated;
	}

	public static void setAuthenticated(boolean authenticated) {
		ServerCommunicator.authenticated = authenticated;
	}

	public static String getPhoneNumber() {
		return phoneNumber;
	}

	public static void setPhoneNumber(String phoneNumber) {
		ServerCommunicator.phoneNumber = phoneNumber;
	}
	
}

class Timer implements Runnable {

	public boolean timeOut = false;

	public void run() {
		try {
			Thread.sleep(10 * 1000); // wait for 10 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timeOut = true;
	}
}
