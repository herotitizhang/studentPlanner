package model;

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

public interface ScheduleI extends Serializable{
	
	public WeeklySchedule eventsDuring();
	
	public List<EventI> getAllSortedEvents();
	
	public List<CategoryI> getCategories();
	
	public HashMap<String, CategoryI> getCategoriesMap();
	
	public CategoryI addCategory(String event) throws NameInUseException;
	
	public CategoryI removeCategory(String category);

	public boolean checkConflict(EventI event);

	public boolean checkConflict(CategoryI category);
	
	
}
