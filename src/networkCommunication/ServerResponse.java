package networkCommunication;

import java.io.Serializable;

import model.ScheduleI;

public class ServerResponse implements Serializable {
	
	private boolean accepted = false; 
	private String failureNotice = null;
	private ScheduleI schedule = null;
	
	public boolean isAccepted() {
		return accepted;
	}
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	public String getFailureNotice() {
		return failureNotice;
	}
	public void setFailureNotice(String failureNotice) {
		if (!accepted)
			this.failureNotice = failureNotice;
	}
	public ScheduleI getSchedule() {
		return schedule;
	}
	public void setSchedule(ScheduleI schedule) {
		this.schedule = schedule;
	}
	
}
