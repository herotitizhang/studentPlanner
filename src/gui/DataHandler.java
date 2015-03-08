/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.CategoryI;
import model.Event;
import model.EventI.Repeat;
import model.EventI;
import model.EventI.Priority;
import model.NameInUseException;
import model.Schedule;

/**
 * Class that handles the addition, removal, alteration and communication 
 * of data between controllers and backend
 * @author Annie Fifer
 */
public class DataHandler {
    
    private final static DataHandler instance = new DataHandler();
    private Schedule schedule;
    private ObservableList<EventI> eventList;
    private ObservableList<EventCell> eventCellList;
    private ObservableList<CategoryI> categoryList;
    private Map<CategoryI, ObservableList> catEventMap;
    private EventI currentEvent;
    private CategoryI currentCategory;
    
    public DataHandler() {
        Schedule sch = new Schedule(); /* to be changed? */
        if (sch != null) {
            setSchedule(sch);
        } else {
            setSchedule(new Schedule());
        }
        setEventList();
        setCategories();
    }
    
    /**
     * @return an instance of DataHandler
     */
    public static DataHandler getInstance() {
        return instance;
    }
    
    public void setSchedule(Schedule sch) {
        schedule = sch;
    }
    
    public Schedule getSchedule() {
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
    
    public void setCurrentCategory(CategoryI category) {
        currentCategory = category;
    }
    
    public CategoryI getCurrentCategory() throws ItemNotFoundException {
        if (currentCategory == null) throw new ItemNotFoundException();
        return currentCategory;
    }
    
    public void updateCurrentCategory(String newName) throws ItemNotFoundException {
        /*
        if (currentCategory == null) throw new ItemNotFoundException();
        */
        //is the backend going to have category editing functionality?
    }
    
    public void deleteCurrentCategory() throws ItemNotFoundException {
        CategoryI category = getCurrentCategory();
        schedule.removeCategory(category.toString());
        categoryList.remove(category);
    }
    
    public void deleteCurrentEvent() throws ItemNotFoundException {
        EventI event = getCurrentEvent();
        setCurrentEvent(null);
        event.getCategory().removeEvent(event);
        eventList.remove(event);
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
    public boolean addEvent(String name, String text, String start, 
			String end, boolean hasAlert, String alertText, String alertTimeString, 
                        Repeat repeat, Priority priority, CategoryI category) throws EmptyFieldException {
        
        if (!hasNecessaryInput(name, start, end)) {
            throw new EmptyFieldException();
        }
        
        String[] startTokens = start.split("-");
        GregorianCalendar startTime = new GregorianCalendar(Integer.parseInt(startTokens[0]),
                        Integer.parseInt(startTokens[1])-1, Integer.parseInt(startTokens[2]), 
                        Integer.parseInt(startTokens[3]), Integer.parseInt(startTokens[4]));
        String[] endTokens = end.split("-");
        GregorianCalendar endTime = new GregorianCalendar(Integer.parseInt(endTokens[0]),
                        Integer.parseInt(endTokens[1])-1, Integer.parseInt(endTokens[2]), 
                        Integer.parseInt(endTokens[3]), Integer.parseInt(endTokens[4]));
        Event newEvent;
        
        if (category == null) {
            newEvent = new Event(name, startTime, endTime, hasAlert, repeat);
        } else {
            if ((!alertText.isEmpty()) && (!alertTimeString.isEmpty())) {
                String[] alertTokens = alertTimeString.split("-");
                GregorianCalendar alertTime = new GregorianCalendar(Integer.parseInt(alertTokens[0]),
                        Integer.parseInt(alertTokens[1])-1, Integer.parseInt(alertTokens[2]), 
                        Integer.parseInt(alertTokens[3]), Integer.parseInt(alertTokens[4]));
                newEvent = new Event(name, text, startTime, endTime, hasAlert, alertText, alertTime, repeat, priority, category);
            } else {
                newEvent = new Event(name, startTime, endTime, hasAlert, repeat, category);
            }
        }
        
        eventList.add(newEvent);
        if (!categoryList.contains(category)) {
            categoryList.add(category);
        }
        
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
                deleteCurrentEvent();
                return;
            }
        } catch (ItemNotFoundException ex) {
            // this is fine
        }
        event.getCategory().removeEvent(event);
        eventList.remove(event);
    }
    
    public boolean updateEvent(String name, String text, String start, 
			String end, boolean hasAlert, String alertText, String alertTimeString, 
                        Repeat repeat, Priority priority, CategoryI category) throws ItemNotFoundException {
        
        if (!hasNecessaryInput(name, start, end)) {
            return false;
        }
        
        EventI event = getCurrentEvent();
        
        String[] startTokens = start.split("-");
        GregorianCalendar startTime = new GregorianCalendar(Integer.parseInt(startTokens[0]),
                        Integer.parseInt(startTokens[1])-1, Integer.parseInt(startTokens[2]), 
                        Integer.parseInt(startTokens[3]), Integer.parseInt(startTokens[4]));
        String[] endTokens = end.split("-");
        GregorianCalendar endTime = new GregorianCalendar(Integer.parseInt(endTokens[0]),
                        Integer.parseInt(endTokens[1])-1, Integer.parseInt(endTokens[2]), 
                        Integer.parseInt(endTokens[3]), Integer.parseInt(endTokens[4]));
        String[] alertTokens = alertTimeString.split("-");
        GregorianCalendar alertTime = new GregorianCalendar(Integer.parseInt(alertTokens[0]),
                        Integer.parseInt(alertTokens[1])-1, Integer.parseInt(alertTokens[2]), 
                        Integer.parseInt(alertTokens[3]), Integer.parseInt(alertTokens[4]));
        
        event.setName(name);
        event.setText(text);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setRepeating(repeat);
        event.setPriority(priority);
        event.setAlert(hasAlert);
        event.setAlertText(alertText);
        event.setAlertTime(alertTime);
        
        return true;
    }
    
    public void removeEvent(EventI event) {
        event.getCategory().removeEvent(event);
        eventList.remove(event);
    }
    
    public void setCategories() {
        categoryList = FXCollections.observableArrayList(schedule.getCategories());
    }
    
    public ObservableList<CategoryI> getCategories() {
        return categoryList;
    }
    
    public CategoryI getCategory(String cat) throws EmptyFieldException {
        if (schedule.getCategoriesMap().get(cat) == null) {
            if (cat.equals("")) throw new EmptyFieldException();
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
    
    private boolean hasNecessaryInput(String name, String start, String end) {
        return (!name.isEmpty() && !start.isEmpty() && !end.isEmpty());
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
