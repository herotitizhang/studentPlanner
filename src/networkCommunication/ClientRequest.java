package networkCommunication;

import java.io.Serializable;

import model.ScheduleI;

public class ClientRequest implements Serializable {
	
	RequestType type = null;
	ScheduleI schedule = null;
	
	// the following 2 fields are only for CREATE/LOGIN
	String userName = null;
	String password = null; 
	
	String phoneNumber = null;
	String authenCode = null;
	
	// for alert
	String categoryName = null;
	String eventName = null;
	
	public ClientRequest(RequestType type) {
		this.type = type;
	}

	public RequestType getType() {
		return type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

	public ScheduleI getSchedule() {
		return schedule;
	}

	public void setSchedule(ScheduleI schedule) {
		this.schedule = schedule;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
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
