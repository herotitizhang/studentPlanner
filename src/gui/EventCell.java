/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import model.Event;

/**
 * Custom event cell for list display
 */
public class EventCell extends ListCell<Event> {
    
    private EventData data;
    
    @Override
    public void updateItem(Event event, boolean empty){
        super.updateItem(event, empty);
        if (event != null) {
            data = new EventData();
            data.setEvent(event);
            setGraphic(data.getBox());
        } else {
            setGraphic(null);
        }
    }
    
    public EventData getEventData() {
        return data;
    }
    
    /**
     * Class that stores event data for access by EventCell
     */
    public class EventData {

        @FXML private HBox hBox;
        @FXML private Label name;
        
        private Event event;

        public EventData() {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/EventCellFXML.fxml"));
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        

        public void setEvent(Event ev){
           name.setText(ev.getName());
        }
        
        public Event getEvent() {
            return event;
        }

        /**
         * @return HBox object with event information
         */
        public HBox getBox() {
            return hBox;
        }
    }
    
}