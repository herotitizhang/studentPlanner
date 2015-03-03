package networkCommunication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import model.ScheduleI;
import networkCommunication.ClientRequest.RequestType;
import utilities.IOSystem;

public class ServerCommunicator {

	private static boolean loggedIn = false; // if set to true, the user can not
												// send LOGIN or CREATE again
	private static String username = null;
	private static String password = null;
	// TODO needs to change the 2 following fields TODO
	private static String serverIP = "localhost";
	private static int port = 1025;

	/**
	 * the invoker will call this method like ServerResponse sr =
	 * ServerCommunicator.sendClientRequest the invoker only proceeds after
	 * ServerResponse is assigned a value; throw the ConnectionException (which
	 * extends IOException) and let the invoker handles it
	 * 
	 * Note: the ConnectionException indicates that internet is not available,
	 * in this case the invoker should switch to local save/load.
	 * 
	 * throw the unknownhostException (should not happen as long as the serverIP
	 * and port are correct)
	 * 
	 * the invoker also needs to check for null due to unexpected disconnection
	 */
	public static ServerResponse sendClientRequest(ClientRequest clientRequest)
			throws IOException, UnknownHostException {

		if ((!loggedIn) || clientRequest == null) return null;
		
		Socket socket = new Socket(serverIP, port);
		OutputStream out = socket.getOutputStream();
		out.write(IOSystem.getByteArray(clientRequest));
		out.flush();

		// start the timer
		Timer timer = new Timer();
		new Thread(timer).start();

		ServerResponse serverResponse = null;
		while (socket.isConnected() && !timer.timeOut) {
			if (socket.getInputStream().available() > 0) {
				InputStream in = socket.getInputStream();
				byte[] content = new byte[in.available()]; 
				in.read(content);
				serverResponse = (ServerResponse) IOSystem.getObject(content);
				socket.close();
			}
		}

		if ((clientRequest.getType() == RequestType.CREATE || clientRequest.getType() == RequestType.LOGIN)
				 && (serverResponse!= null) /* && ( serverResponse is ACCEPT TODO TODO TODO) */ ) {
			loggedIn = true;
			username = clientRequest.getUserName();
			password = clientRequest.getPassword();
		}
		 
		return serverResponse;
	 
	}

	public ClientRequest generateCreateRequest(String username, String password) {
		ClientRequest request = null;
		if (!loggedIn) {
			request = new ClientRequest(RequestType.CREATE);
			request.setUserName(username);
			request.setPassword(password);
		}
		return request;
	}

	public ClientRequest generateSaveRequest(ScheduleI schedule) {
		ClientRequest request = new ClientRequest(RequestType.SAVE);
		request.setSchedule(schedule);
		return request;
	}
	
	public ClientRequest generateLoadRequest() {
		ClientRequest request = new ClientRequest(RequestType.LOAD);
		return request;
	}
	
	public ClientRequest generateRequestAuthRequest(String phoneNumber) { // TODO support email 
		ClientRequest request = new ClientRequest(RequestType.REQUEST_AUTH);
		request.setPhoneNumber(phoneNumber);
		return request;
	}
	
	public ClientRequest generateAuthenticateRequest(String authenCode) {
		ClientRequest request = new ClientRequest(RequestType.AUTH);
		request.setAuthenCode(authenCode);
		return request;
	}
	
	public ClientRequest generateAlertRequest(ScheduleI schedule) {
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
			Thread.sleep(5 * 1000); // wait for 5 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timeOut = true;
	}
}
