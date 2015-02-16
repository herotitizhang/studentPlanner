/**
 * Category holds a set of events. If the category is deleted, 
 * all associated events are deleted as well.
 * 
 * Author: Tony Zhang
 * 
 */
package plannerLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Category {
	
	private String name = ""; //TODO different from architecture. added the name attribute.
	private List<Event> events = new ArrayList<Event>(); 
	
	public Category (String name) {
		this.name = name;
	}
	
	public void addEvent(Event event) {
		if (!events.contains(event) && event != null) {
			events.add(event);
			event.setCategory(this);
		}
	}
	
	public void removeEvent(Event event) {
		if (events.contains(event)) {
			events.remove(event); // this line must come before the next line
			event.setCategory(null);
		}
	}
	
	public List<Event> getAllEvents() {
		Collections.sort(events);  
		return events;
	}
	
	public void removeAllEvents() {
		events.clear();
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
