/**
 * Full list of events, ordered by start time, with helper methods
 * 
 * Author: Tony Zhang
 */

package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO add interfaces for each class

public class Schedule {

	HashMap<String, Category> categories = new HashMap<String, Category>(); // TODO add a default category in the beginning
	List<Event> uncategorizedEvents = new ArrayList<Event>(); 
	
	/**
	 * Returns an ordered list of all of the events 
	 * taking place during the specified week (by start time)
	 * @return
	 */
	public WeeklySchedule eventsDuring(/*Week*/) { // TODO look for a Week object
		
		return null;
	}

	/**
	 * Saves Schedule to server (or Disk)
	 */
	public void save() {
		//TODO save to server
	}
	
	/**
	 * Connects to server (or disk) and retrieves schedule
	 */
	public static Schedule load() {
		//TODO save to server
		return null;
	}
	
	/**
	 * @return a master list of events
	 */
	public List<Event> getEvents() {
		ArrayList<Event> toBeReturned = new ArrayList<Event>();
		toBeReturned.addAll(uncategorizedEvents); // TODO different from architecture
		
		for (Category category: categories.values()) {
			toBeReturned.addAll(category.getAllEvents());
		}
		
		Collections.sort(toBeReturned);
		return toBeReturned;
	}
	
	/**
	 * @return a list of categories
	 */
	public List<Category> getCategories() {
		return new ArrayList<Category>(categories.values());
	}
	
	// TODO different from architecture. can keep hashmap
	public HashMap<String, Category> getCategoriesMap() {
		return categories;
	}
	
 	/**
	 * Creates a new category of the given name, and returns it
	 * @param category
	 * @return
	 * @throws NameInUseException
	 */
	public Category addCategory(String category) throws NameInUseException {
		if (categories.containsKey(category)) throw new NameInUseException(category);
		categories.put(category, new Category(category));
		return categories.get(category);
	}
	
	/**
	 * Removes the category with the referenced name
	 */
	public void removeCategory(String category) {
		categories.remove(category);
	}
}
