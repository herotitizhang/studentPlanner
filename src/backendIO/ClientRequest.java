/**
 * ClientRequest is a container that includes what's sent from the client to the server.
 * Author: Tony Zhang
 */
package backendIO;

import java.io.Serializable;
import java.util.GregorianCalendar;


public class ClientRequest implements Serializable {
	
	RequestType type = null;
	
	// the following 2 fields are only for CREATE/LOGIN
	String userName = null;
	String password = null; 
	
	// for save/load
	byte[] schedule = null;
	
	// for authentication
	String phoneNumber = null;
	String authenCode = null;
	
	// for alert
	String alertTitle = null;
	String alertText = null;
	GregorianCalendar alertTime = null;
	String repeat = null;
	
	public ClientRequest(RequestType type) {
		this.type = type;
	}

	public RequestType getType() {
		return type;
	}


	public void setType(RequestType type) {
		this.type = type;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public byte[] getSchedule() {
		return schedule;
	}


	public void setSchedule(byte[] schedule) {
		this.schedule = schedule;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getAuthenCode() {
		return authenCode;
	}


	public void setAuthenCode(String authenCode) {
		this.authenCode = authenCode;
	}


	public String getAlertTitle() {
		return alertTitle;
	}


	public void setAlertTitle(String alertTitle) {
		this.alertTitle = alertTitle;
	}


	public String getAlertText() {
		return alertText;
	}


	public void setAlertText(String alertText) {
		this.alertText = alertText;
	}


	public GregorianCalendar getAlertTime() {
		return alertTime;
	}


	public void setAlertTime(GregorianCalendar alertTime) {
		this.alertTime = alertTime;
	}


	public String getRepeat() {
		return repeat;
	}


	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	

	public enum RequestType {
		CREATE, LOGIN, SAVE, LOAD, REQUEST_AUTH, AUTH, ALERT;
		// for the client side:
		// CREATE: set username and password
		// LOGIN: set username and password
		// SAVE: set schedule
		// LOAD:
		// REQUEST_AUTH:
		// AUTH: 
		
		// for the client side:
		// CREATE: 
		// LOGIN
		// SAVE: 
		// LOAD:
		// REQUEST_AUTH:
		// AUTH: 
	}

}
