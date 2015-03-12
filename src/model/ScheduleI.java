/**
 * Schedule Interface. Different kinds of schedules might be created.
 * Author: Tony Zhang
 */

package model;

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import model.EventI.Priority;

public interface ScheduleI extends Serializable{
	
	public List<EventI> getAllSortedEvents();

	public List<EventI> getEvents(String categoryToLookFor);

	public List<EventI> getEvents(CategoryI categoryToLookFor);

	public List<EventI> getEvents(Priority priority);

	public List<EventI> getEvents(GregorianCalendar startTime, GregorianCalendar endTime);

	public List<CategoryI> getCategories();
	
	public HashMap<String, CategoryI> getCategoriesMap();
	
	public CategoryI addCategory(String category) throws NameInUseException;
	
	public CategoryI addCategory(Category category) throws NameInUseException;
	
	public CategoryI removeCategory(String category);

	boolean editCategoryName(String originalName, String newName);
	
	boolean checkConflict(EventI event1, EventI event2);

	public boolean checkConflict(CategoryI category);



	
}
