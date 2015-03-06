/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

/**
 * Custom control for date and time input
 * @author anniefifer1
 */
public class DateTimeInput extends GridPane {
    @FXML
    private ComboBox<String> monthInput;
    @FXML
    private ComboBox<String> dayInput;
    @FXML
    private ComboBox<String> yearInput;
    @FXML
    private ComboBox<String> hourInput;
    @FXML
    private ComboBox<String> minuteInput;
    
    public void DateTimeInput() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/DateTimeInputFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        /* set items */
        monthInput.setItems(returnSequentialArray(1, 12));
        dayInput.setItems(returnSequentialArray(1, 31));
        yearInput.setItems(returnSequentialArray(2015, 2020));
        hourInput.setItems(returnSequentialArray(1, 23));
        minuteInput.setItems(returnSequentialArray(0, 59));
    }
    
    /**
     * 
     * @param begin
     * @param end
     * @return an array of sequential numbers as strings, with leading zeros
     * for single-digit numbers
     */
    private ObservableList<String> returnSequentialArray(int begin, int end) {
        List<String> toBeReturned = new ArrayList<String>();
        for (int i=begin; i < end+1; i++) {
            String str = Integer.toString(i);
            if (i < 10) {
                str = "0" + str;
            }
            toBeReturned.add(str);
        }
        return FXCollections.observableArrayList(toBeReturned);
    }
    
    /**
     * Updates day options when month is selected
     */
    private void updateDayOptions() {
        // Not yet implemented
    }
    
    /**
     * @return an array formatted for creating GregorianCalendar object 
     */
    public String[] getDateArray() {
        return new String[] {yearInput.getValue(), monthInput.getValue(), 
            dayInput.getValue(), hourInput.getValue(), minuteInput.getValue()};
    }

    
}
