package model;

import java.io.Serializable;
import java.util.GregorianCalendar;

import model.EventI.Priority;

public interface EventI extends Serializable, Comparable  {

	// this interface was here because we needed to implement writeTo/readFrom
	// we switched to serialization later so no methods are specified here.
	// keep it for now since there might be some methods to be added in the future
	
	// TODO not sure if it's good practice to put getters/setters, enum and Comparator
	// in an interface.
	
	public String getName();

	public void setName(String name);

	public String getText();

	public void setText(String text);

	public GregorianCalendar getStartTime();

	public void setStartTime(GregorianCalendar startTime);

	public GregorianCalendar getEndTime();

	public void setEndTime(GregorianCalendar endTime);

	public boolean hasAlert();

	public void setAlert(boolean alert);

	public String getAlertText();

	public void setAlertText(String alertText);

	public GregorianCalendar getAlertTime();

	public void setAlertTime(GregorianCalendar alertTime);

	public Repeat getRepeating() ;

	public void setRepeating(Repeat repeating);

	public Priority getPriority();

	public void setPriority(Priority priority);
	
	public CategoryI getCategory();

	public void setCategory(CategoryI category);
	
	public enum Repeat {
		NONE, WEEKLY, MONTHLY, YEARLY;
	}
	
	public enum Priority {
		LOW, MEDIUM, HIGH, URGENT;
	}

	
}
