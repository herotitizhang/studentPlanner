package networkCommunication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import model.ScheduleI;
import networkCommunication.ClientRequest.RequestType;
import utilities.ClientIOSystem;


// Note: processing serverResponse should be separate from the functionality provided in this class
public class ServerCommunicator {

	
	// if loggedIn is true, the user can not send LOGIN or CREATE request
	// if loggedIn is false, the user can not send
	private static boolean loggedIn = false;
	
	private static String username = null;
	private static String password = null;
	// TODO needs to change the 2 following fields TODO
	private static String serverIP = "10.0.0.3";
	private static int port = 12345;

	/**
	 * the invoker will call this method like ServerResponse sr =
	 * ServerCommunicator.sendClientRequest the invoker only proceeds after
	 * ServerResponse is assigned a value; throw the ConnectionException (which
	 * extends IOException) and let the invoker handles it
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
			while ((!socket.isClosed()) && socket.isConnected() && !timer.timeOut) {
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
			password = clientRequest.getPassword();
		}
		 
		return serverResponse;
	 
	}

	public static ClientRequest generateCreateRequest(String username, String password) {
		ClientRequest request = null;
		if (!loggedIn) {
			request = new ClientRequest(RequestType.CREATE);
			request.setUserName(username);
			request.setPassword(password);
		}
		return request;
	}
	
	public static ClientRequest generateLoginRequest(String username, String password) {
		ClientRequest request = null;
		if (!loggedIn) {
			request = new ClientRequest(RequestType.LOGIN);
			request.setUserName(username);
			request.setPassword(password);
		}
		return request;
	}

	public static ClientRequest generateSaveRequest(ScheduleI schedule) {
		ClientRequest request = new ClientRequest(RequestType.SAVE);
		request.setSchedule(schedule);
		return request;
	}
	
	public static ClientRequest generateLoadRequest() {
		ClientRequest request = new ClientRequest(RequestType.LOAD);
		return request;
	}
	
	public static ClientRequest generateRequestAuthRequest(String phoneNumber) { // TODO support email 
		ClientRequest request = new ClientRequest(RequestType.REQUEST_AUTH);
		request.setUserName(username);
		request.setPassword(password); // not necessary but it's good to include
		request.setPhoneNumber(phoneNumber);
		return request;
	}
	
	public static ClientRequest generateAuthenticateRequest(String authenCode) {
		ClientRequest request = new ClientRequest(RequestType.AUTH);
		request.setAuthenCode(authenCode);
		return request;
	}
	
	public static ClientRequest generateAlertRequest(ScheduleI schedule) {
		ClientRequest request = new ClientRequest(RequestType.ALERT);
		request.setSchedule(schedule);
		return request;
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
