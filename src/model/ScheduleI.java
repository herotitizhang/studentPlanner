package model;

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

public interface ScheduleI extends Serializable{

	public void writeTo(Writer writer);
	
	public void loadFrom(Reader reader);
	
	public boolean addObligation(EventI event);
	
	public boolean checkConflict(EventI event);

	public boolean checkConflict(ObligationI obligation);
	
	public boolean removeObligation(ObligationI obligation);
	
	
}
