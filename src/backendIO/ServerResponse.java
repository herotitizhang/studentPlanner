/**
 * ServerResponse is the container that includes what's sent from the server to the client
 * Author: Tony Zhang
 */

package backendIO;

import java.io.Serializable;


public class ServerResponse implements Serializable {
	
	private boolean accepted = false; 
	private String failureNotice = null;
	private byte[] schedule = null;
	
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
	public byte[] getSchedule() {
		return schedule;
	}
	public void setSchedule(byte[] schedule) {
		this.schedule = schedule;
	}
	
}
