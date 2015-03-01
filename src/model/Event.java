/**
 *  Simple class that holds start and end time, whether or not 
 *  there's an associated alert, and how often the event repeats (if at all)
 *  
 *  Author: Tony Zhang
 */

package model;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class Event implements EventI{
	
	private String name = null;
	private String text = null;
	private GregorianCalendar startTime = null;
	private GregorianCalendar endTime = null;
	
	// alertText and alertTime are set only when alert is true
	private boolean alert = false;
	private String alertText = null;
	private GregorianCalendar alertTime = null;
	
	// no repeat by default
	private Repeat repeating = Repeat.NONE;  
	
	// medium priority by default
	private Priority priority = Priority.MEDIUM;
	
	// TODO needs to instantiate the default category
	private CategoryI category = null;  
	
	/**
	 * A constructor that sets basic and necessary attributes
	 * @param name
	 * @param startTime
	 * @param endTime
	 * @param alert
	 * @param repeating
	 */
	public Event(String name, GregorianCalendar startTime, GregorianCalendar endTime,
			boolean alert, Repeat repeating){
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime; // TODO check that endTime is after startTime
		this.alert = alert;
		this.repeating = repeating;
	}
	
	/**
	 * A constructor that specifies the category
	 * @param name
	 * @param startTime
	 * @param endTime
	 * @param alert
	 * @param repeating
	 * @param category
	 */
	public Event(String name, GregorianCalendar startTime, GregorianCalendar endTime,
			boolean alert, Repeat repeating, CategoryI category){
		this(name, startTime, endTime, alert, repeating);
		setCategory(category);
	}
	
	/**
	 * A constructor that sets all attributes
	 * @param name
	 * @param text
	 * @param startTime
	 * @param endTime
	 * @param alert
	 * @param alertText
	 * @param alertTime
	 * @param repeating
	 * @param category
	 */
	public Event(String name, String text, GregorianCalendar startTime, 
			GregorianCalendar endTime, boolean alert, String alertText,
			GregorianCalendar alertTime, Repeat repeating, 
			Priority priority, CategoryI category){
		this(name, startTime, endTime, alert, repeating, category);
		this.text = text;
		this.priority = priority;
		if (alert) {
			this.alertText = alertText;
			this.alertTime = alertTime;
		}
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public GregorianCalendar getStartTime() {
		return startTime;
	}

	@Override
	public void setStartTime(GregorianCalendar startTime) {
		this.startTime = startTime;
	}

	@Override
	public GregorianCalendar getEndTime() {
		return endTime;
	}

	@Override
	public void setEndTime(GregorianCalendar endTime) {
		this.endTime = endTime;
	}

	@Override
	public boolean hasAlert() {
		return alert;
	}

	@Override
	public void setAlert(boolean alert) {
		this.alert = alert;
	}

	@Override
	public String getAlertText() {
		if (hasAlert()) return alertText;
		return null; // TODO throw an exception? necessary because an Alert text being null
		              // can either mean the alert is not set or the alertText is not set
	}
	
	@Override
	public void setAlertText(String alertText) {
		if (hasAlert()) {
			this.alertText = alertText;
		} else {
			// TODO throw an exception?
		}
			
	}

	@Override
	public GregorianCalendar getAlertTime() {
		if (hasAlert()) return alertTime;
		return null; // TODO throw an exception?

	}

	@Override
	public void setAlertTime(GregorianCalendar alertTime) {
		if (hasAlert()) {
			this.alertTime = alertTime;
		} else {
			// TODO throw an exception?
		}
	}

	@Override
	public Repeat getRepeating() {
		return this.repeating;
	}

	@Override
	public void setRepeating(Repeat repeating) {
		this.repeating = repeating;
	}
	
	@Override
	public Priority getPriority() {
		return this.priority;
	}

	@Override
	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	@Override
	public CategoryI getCategory() {
		return category;
	}

	@Override
	public void setCategory(CategoryI category) {
		if (this.category == category) return; // for case 3 shown below
		
		if (this.category == null && category != null) { // case 1
			this.category = category;
			category.addEvent(this);
		} else if (this.category != null && category == null) { // case 2
			this.category.removeEvent(this);
			this.category = null;
		} else if (this.category != null && category != null) { // case 3
			this.category.removeEvent(this);
			this.category = category;
			this.category.addEvent(this);
		} else { // case 4: this.category == null && category == null
			// do nothing
		}
	}
	
	@Override
	public String toString() {
        DateFormat formatter = DateFormat.getInstance(); 

		StringBuilder sb = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		sb.append("===================").append(newLine)
		.append("Name: ").append(this.name).append(newLine)
		.append("Text: ").append(text).append(newLine)
		.append("Start time: ").append(formatter.format(startTime.getTime())).append(newLine)
		.append("End time: ").append(formatter.format(endTime.getTime())).append(newLine)
		.append("Alert exists: ").append(alert).append(newLine);
		
		if (alert) {
			sb.append("Alert text: ").append(alertText).append(newLine);
			if (alertTime != null) {
				sb.append("Alert time: ").append(formatter.format(alertTime.getTime())).append(newLine);

			}
		}
		
		sb.append("Repeating: ").append(repeating).append(newLine)
		.append("Category: ").append(category.toString());
		
		return sb.toString();
	
	}
	
	@Override
	public int compareTo(Object o) {
		
		if (this == o) return 0;
		
		if (o != null && o instanceof Event) { // compare the start times
			int startTimeComparison = this.getStartTime().compareTo(((Event)o).getStartTime());
			if (startTimeComparison != 0) {
				return startTimeComparison;
			} else { // compare the end times
				int endTimeComparison = this.getEndTime().compareTo(((Event)o).getEndTime());
				if (endTimeComparison != 0) {
					return endTimeComparison;
				} else { // compare the names
					return this.name.compareTo(((Event)o).getName());
				}
			}
		}
		System.out.println("Not the same object. Unable to compare");
		return 0;
	}
	
	
	public class EventComparator implements Comparator<Event> {

		@Override
		public int compare(Event o1, Event o2) {
			return o1.compareTo(o2);
		}
		
	}

	// TODO checkers !!!

}
