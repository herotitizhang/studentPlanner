/**
 * Full list of events, ordered by start time, with helper methods
 * 
 * Author: Tony Zhang
 */

package model;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// TODO add interfaces for each class

public class Schedule implements ScheduleI{

	HashMap<String, CategoryI> categories = new HashMap<String, CategoryI>(); // TODO add a default category in the beginning
	
	public Schedule() {
		categories.put("default", new Category("default"));
	}
	
	/**
	 * Returns an ordered list of all of the events 
	 * taking place during the specified week (by start time)
	 * @return
	 */
	@Override
	public WeeklySchedule eventsDuring(/*Week*/) { // TODO look for a Week object
		
		return null;
	}

	/**
	 * @return a master list of events
	 */
	@Override
	public List<EventI> getAllSortedEvents() {
		ArrayList<EventI> toBeReturned = new ArrayList<EventI>();
		
		for (CategoryI category: categories.values()) {
			toBeReturned.addAll(category.getAllEvents());
		}
		
		Collections.sort(toBeReturned); // TODO ttest if it's sorted (if Event's compareTo is invoked)
		return toBeReturned;
	}
	
	/**
	 * @return a list of categories
	 */
	@Override
	public List<CategoryI> getCategories() {
		return new ArrayList<CategoryI>(categories.values());
	}
	
	/**
	 * @return the map
	 */
	@Override
	public HashMap<String, CategoryI> getCategoriesMap() {
		return categories;
	}
	
 	/**
	 * Creates a new category of the given name, and returns it
	 * @param category
	 * @return
	 * @throws NameInUseException
	 */
	@Override
	public CategoryI addCategory(String category) throws NameInUseException {
		if (category.trim().length() == 0) category = "default";
		if (categories.containsKey(category)) throw new NameInUseException(category);
		categories.put(category, new Category(category));
		return categories.get(category);
	}
		
	/**
	 * Removes the category with the referenced name
	 */
	@Override
	public CategoryI removeCategory(String category) {
		if (category.equals("default")) return null;
		return categories.remove(category);
	}

	@Override
	public boolean checkConflict(EventI event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkConflict(CategoryI obligation) {
		// TODO Auto-generated method stub
		return false;
	}

}
