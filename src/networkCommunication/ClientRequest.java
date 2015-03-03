package networkCommunication;

import java.io.Serializable;

import model.ScheduleI;

public class ClientRequest implements Serializable {
	
	RequestType type = null;
	ScheduleI schedule = null;
	UserInfo userInfo = null;
	
	public ClientRequest(RequestType type, ScheduleI schedule, UserInfo userInfo) {
		this.type = type;
		if (type == RequestType.SAVE) {
			this.schedule = schedule; 
		} else if (type == RequestType.LOAD) {
			
		} else if (type == RequestType.CREATE) {
			this.userInfo = userInfo;
		} else if (type == RequestType.REQUEST_AUTH) {
			
		} else if (type == RequestType.AUTH) {
			
		}
	}
	
	
	public enum RequestType {
		SAVE, LOAD, CREATE, REQUEST_AUTH, AUTH;
	}
}
