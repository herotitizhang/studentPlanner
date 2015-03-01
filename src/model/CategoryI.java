package model;

import java.io.Serializable;
import java.io.Writer;
import java.io.Reader;
import java.util.List;

public interface CategoryI extends Serializable {

	public boolean addEvent(EventI event);
	
	public boolean removeEvent(EventI event);
	
	public boolean checkEvent(EventI event);
	/*
	The checkers are meant to be be "Check if this other event overlaps with 
	this one". Feel free to ignore that, for now, we decided not to check for 
	time conflicts anyway.
	*/
	public List<EventI> getCore();
	/*
	 Categories have their own times. core represents those times
	 this method might be cut in the future
	 */
	
	public List<EventI> getChildren();
	/*
	 return a list of events that belong to this category
	 might be cut in the future
	 */
	
	public String getName();
	
	public List<EventI> getAllEvents();
	
	public void removeAllEvents();

}

