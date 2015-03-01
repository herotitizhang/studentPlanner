/**
 * Category holds a set of events. If the category is deleted, 
 * all associated events are deleted as well.
 * 
 * Author: Tony Zhang
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Category implements CategoryI{
	
	private String name = ""; //TODO different from architecture. added the name attribute.
	private List<EventI> events = new ArrayList<EventI>(); 
	
	public Category (String name) {
		this.name = name;
	}
	
	@Override
	public boolean addEvent(EventI event) {
		if (!events.contains(event) && event != null) {
			events.add(event);
			event.setCategory(this);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeEvent(EventI event) {
		if (events.contains(event)) {
			events.remove(event); // this line must come before the next line
			event.setCategory(null);
			return true;
		}
		return false;
	}
	
	@Override
	public List<EventI> getAllEvents() {
		Collections.sort(events);  // sorted based on Event.compareTo
		return events;
	}
	
	@Override
	public void removeAllEvents() {
		events.clear();
	}
	
	// the same as toString implementation, but it's better to keep them separate
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean checkEvent(EventI event) {
		return false;
	}

	@Override
	public List<EventI> getCore() {
		return null;
	}

	@Override
	public List<EventI> getChildren() {
		return null;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
