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
import javafx.scene.control.Button;
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
    
    public void initializeSchedule() {
        
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                
        GregorianCalendar start = new GregorianCalendar();
        GregorianCalendar end = new GregorianCalendar();
        
        HBox[][] blocks = new HBox[7][];
        
        for (int day=0; day<7; day++) {
            
            /* logic for displaying events for each day */
            start.setTime(cal.getTime());
            cal.add(Calendar.DAY_OF_WEEK, 1);
            end.setTime(cal.getTime());
            List<EventI> events = DataHandler.getInstance().getEventsDuring(start, end);
            
            blocks[day] = new HBox[events.size()];
            
            int index = 0;
            for (EventI event : events) {
                blocks[day][index] = new HBox();
                blocks[day][index].getChildren().add(new Label(event.getName()));
                index++;
            }
        }
        
        System.out.println(blocks[5].length);
        
        sundayBox.getChildren().addAll(blocks[0]);
        mondayBox.getChildren().addAll(blocks[1]);
        tuesdayBox.getChildren().addAll(blocks[2]);
        wednesdayBox.getChildren().addAll(blocks[3]);
        thursdayBox.getChildren().addAll(blocks[4]);
        fridayBox.getChildren().addAll(blocks[5]);
        saturdayBox.getChildren().addAll(blocks[6]);
    }
    
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeSchedule();
    }
    
}
