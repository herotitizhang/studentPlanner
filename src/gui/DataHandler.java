/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.CategoryI;
import model.Event;
import model.EventI.Repeat;
import model.EventI;
import model.EventI.Priority;
import model.NameInUseException;
import model.Schedule;
import model.ScheduleI;

/**
 * Class that handles the addition, removal, alteration and communication 
 * of data between controllers and backend
 * @author Annie Fifer
 */
public class DataHandler {
    
    private final static DataHandler instance = new DataHandler();
    private ScheduleI schedule;
    private ObservableList<EventI> eventList;
    private ObservableList<EventCell> eventCellList;
    private ObservableList<CategoryI> categoryList;
    private Map<CategoryI, ObservableList> catEventMap;
    private EventI currentEvent;
    private EventI lastAddedEvent;
    private CategoryI currentCategory;
    
    public DataHandler() {
        /*
        ScheduleI sch = new Schedule();
        if (sch != null) {
            setSchedule(sch);
        } else {
            setSchedule(new Schedule());
        }
        setEventList();
        setCategories();
        */
    }
    
    /**
     * @return an instance of DataHandler
     */
    public static DataHandler getInstance() {
        return instance;
    }
    
    /**
     * Sets schedule for data, sets event and category content
     */
    public void setSchedule(ScheduleI sch) {
        schedule = sch;
        setEventList();
        setCategories(); 
    }
    
    public ScheduleI getSchedule() {
        return schedule;
    }
    
    /**
     * Binds stored list of events to events in schedule
     */
    public void setEventList() {
        eventList = FXCollections.observableArrayList(schedule.getAllSortedEvents());
    }
    
    public void setCurrentEvent(EventI event) {
        currentEvent = event;
    }
    
    public EventI getCurrentEvent() throws ItemNotFoundException {
        if (currentEvent == null) throw new ItemNotFoundException();
        return currentEvent;
    }
    
    public void setLastAddedEvent(EventI event) {
        lastAddedEvent = event;
    }
    
    public EventI getLastAddedEvent() {
        if (lastAddedEvent == null) return null;
        return lastAddedEvent;
    }
    
    public void setCurrentCategory(CategoryI category) {
        currentCategory = category;
    }
    
    public CategoryI getCurrentCategory() throws ItemNotFoundException {
        if (currentCategory == null) throw new ItemNotFoundException();
        return currentCategory;
    }
    
    /**
     * Change the name of current category
     * @param newName
     * @throws ItemNotFoundException 
     */
    public void updateCurrentCategory(String newName) throws ItemNotFoundException {
        schedule.editCategoryName(currentCategory.getName(), newName);
        categoryList.setAll(schedule.getCategories());
    }
    
    public void deleteCurrentCategory() throws ItemNotFoundException {
        CategoryI category = getCurrentCategory();
        schedule.removeCategory(category.toString());
        categoryList.remove(category);
    }
    
    public ObservableList<EventI> getEventList() {
        return eventList;
    }
    
    public List<EventI> getEventList(CategoryI ctg) {
        return ctg.getAllEvents();
    }
    
    public List<EventI> getEventsDuring(GregorianCalendar start, GregorianCalendar end) {
        return schedule.getEvents(start, end);
    }
    
    /**
     * Adds event to schedule using information provided
     */
    public boolean addEvent(String name, String text, LocalDate startDate, String startHour, String startMinute,
			LocalDate endDate, String endHour, String endMinute, boolean hasAlert, String alertText, 
                        LocalDate alertDate, String alertHour, String alertMinute, Repeat repeat, 
                        Priority priority, CategoryI category) throws EmptyFieldException {
        
        if (!hasNecessaryInput(name)) {
            throw new EmptyFieldException();
        }
        
        GregorianCalendar start = returnGregorianCalendar(startDate, startHour, startMinute);
        GregorianCalendar end = returnGregorianCalendar(endDate, endHour, endMinute);
        GregorianCalendar alertTime = returnGregorianCalendar(alertDate, alertHour, alertMinute);
        
        Event newEvent;
        
        if (category == null) {
            category = getCategory("default");
        }
        
        if ((alertTime == null) || (alertText.isEmpty())) {
            newEvent = new Event(name, start, end, hasAlert, repeat, category);
        } else {
            newEvent = new Event(name, text, start, end, hasAlert, alertText, alertTime, repeat, priority, category);
        }
        
        eventList.add(newEvent);
        if (!categoryList.contains(category)) {
            categoryList.add(category);
        }
        
        setLastAddedEvent(newEvent);
        return true;
    }
    
    public boolean addEvent(EventI event) {
        eventList.add(event);
        if (!categoryList.contains(event.getCategory())) {
            categoryList.add(event.getCategory());
        }
        return true;
    }
    
    public void deleteEvent(EventI event) {
        try {
            if (event == getCurrentEvent()) {
                setCurrentEvent(null);
            }
        } catch (ItemNotFoundException ex) {
            // this is fine
        }
        event.getCategory().removeEvent(event);
        eventList.remove(event);
    }
    
    public boolean updateEvent(String name, String text, LocalDate startDate, String startHour, String startMinute,
			LocalDate endDate, String endHour, String endMinute, boolean hasAlert, String alertText, 
                        LocalDate alertDate, String alertHour, String alertMinute, Repeat repeat, 
                        Priority priority, CategoryI category) throws ItemNotFoundException {
        
        if (!hasNecessaryInput(name)) {
            return false;
        }
        
        EventI event = getCurrentEvent();
        
        GregorianCalendar start = returnGregorianCalendar(startDate, startHour, startMinute);
        GregorianCalendar end = returnGregorianCalendar(endDate, endHour, endMinute);
        GregorianCalendar alertTime = returnGregorianCalendar(alertDate, alertHour, alertMinute);
        
        event.setName(name);
        event.setText(text);
        event.setStartTime(start);
        event.setEndTime(end);
        event.setRepeating(repeat);
        event.setPriority(priority);
        event.setAlert(hasAlert);
        event.setAlertText(alertText);
        event.setAlertTime(alertTime);
        
        return true;
    }
    
    public void removeEvent(EventI event) {
        eventList.remove(event);
        System.out.println("event: " + event.getName());
        for (int i=0; i<eventList.size(); i++) {
            System.out.println("event " + i + " is null: " + eventList.get(i) == null);
            System.out.println("category " + i + " is null: " + eventList.get(i).getCategory() == null);
        }
        event.getCategory().removeEvent(event);
        
    }
    
    public void setCategories() {
        categoryList = FXCollections.observableArrayList(schedule.getCategories());
    }
    
    public ObservableList<CategoryI> getCategories() {
        return categoryList;
    }
    
    public CategoryI getCategory(String cat) {
        if (schedule.getCategoriesMap().get(cat) == null) {
            if (cat.equals("")) return null;
            try {
                schedule.addCategory(cat);
            } catch (NameInUseException e) {
                //this won't ever happen
            }
        }
        return schedule.getCategoriesMap().get(cat);       
    }
    
    public CategoryI addCategory(String name) throws NameInUseException {
        CategoryI newCat = schedule.addCategory(name);
        categoryList.add(newCat);
        return newCat;
    }
    
    public void removeCategory(String name) {
        schedule.removeCategory(name);
        categoryList.remove(schedule.getCategoriesMap().get(name));
    }
    
    public LocalDate calendarToDate(GregorianCalendar cal) {
        if (cal == null) {
            return null;
        }
        LocalDate ld = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ld;
    }
    
    /**
     * Creates and returns a GregorianCalendar object with provided date and time
     * @param ld
     * @param hour
     * @param minute
     * @return 
     */
    public GregorianCalendar returnGregorianCalendar(LocalDate ld, String hour, String minute) {
        if ((ld == null) || (hour.isEmpty()) || (minute.isEmpty())) {
            return null;
        }
        GregorianCalendar toBeReturned = new GregorianCalendar();
        toBeReturned.set(ld.getYear(), ld.getMonthValue()-1, ld.getDayOfMonth());
        toBeReturned.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        toBeReturned.set(Calendar.MINUTE, Integer.parseInt(minute));
        return toBeReturned;
    }
    
    /**
     * Determines if information is sufficient for creating an event
     * @param name
     * @return 
     */
    private boolean hasNecessaryInput(String name) { // arguments and conditions
        return (!name.isEmpty());                    // may be added to this method
    }
    
    public void printNumberOfEventsSchedule() {
        System.out.println("schedule.getEvents().size(): " + schedule.getAllSortedEvents().size());
    }
    
    public void printNumberOfEventsData() {
        System.out.println("eventList.size(): " + eventList.size());
    }
    
    public void printNumberOfCategoriesSchedule() {
        System.out.println("schedule.getCategories().size(): " + schedule.getCategories().size());
    }
    
    public void printNumberOfCategoriesData() {
        System.out.println("categoryList.size(): " + categoryList.size());
    }

    
}
