/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import gui.DataHandler;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.EventI;

/**
 * FXML Controller class
 *
 * @author anniefifer1
 */
public class ScheduleViewFXMLController implements Initializable {
    
    @FXML VBox sundayBox;
    @FXML VBox mondayBox;
    @FXML VBox tuesdayBox;
    @FXML VBox wednesdayBox;
    @FXML VBox thursdayBox;
    @FXML VBox fridayBox;
    @FXML VBox saturdayBox;
    
    GregorianCalendar[] week = new GregorianCalendar[8];
    VBox[] dayBoxes;
    
    public void initializeSchedule() {
        dayBoxes = new VBox[]{sundayBox, mondayBox, tuesdayBox, wednesdayBox, thursdayBox, fridayBox, saturdayBox};
        for (int day=0; day<7; day++) {
            List<EventI> events = DataHandler.getInstance().getEventsDuring(week[day], week[day+1]);
            for (EventI event : events) {
                addEventToDay(event, day);
            }
        }
    }
    
    public void addEventToDay(EventI event, int day) {
        HBox box = new HBox();
        box.getChildren().add(new Label(event.getName()));
        dayBoxes[day].getChildren().add(box);
    }
    
    public void addEventToSchedule(EventI event) {
        for (int day=0; day<7; day++) {
            if (DataHandler.getInstance().getEventsDuring(week[day], week[day+1]).contains(event)) {
                addEventToDay(event, day);
            }
        }
    }
    
    private void initializeWeek() {
        GregorianCalendar dayStart = new GregorianCalendar();
        dayStart.set(Calendar.HOUR_OF_DAY, 0);
        dayStart.clear(Calendar.MINUTE);
        dayStart.clear(Calendar.SECOND);
        dayStart.set(Calendar.DAY_OF_WEEK, dayStart.getFirstDayOfWeek());
        
        for (int day=0; day<8; day++) {
            GregorianCalendar cur = new GregorianCalendar();
            cur.setTime(dayStart.getTime());
            week[day] = cur;
            dayStart.add(Calendar.DAY_OF_WEEK, 1);
            System.out.println(week[day].getTime().toString());
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeWeek();
        initializeSchedule();
    }
    
}
