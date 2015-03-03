package networkCommunication;

import java.io.Serializable;

public class UserInfo implements Serializable {
	
	String username = null;
	String password = null;
	String phoneNumber = null;
	String emailAddress = null;
	
	public UserInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public UserInfo(String username, String password, String phoneNumber) {
		this(username, password);
		this.phoneNumber = phoneNumber;
	}
	
	public UserInfo(String username, String password, String phoneNumber, String emailAddress) {
		this(username, password, phoneNumber);
		this.emailAddress = emailAddress;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
}
