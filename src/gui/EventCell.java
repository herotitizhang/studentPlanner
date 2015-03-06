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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.EventI;

/**
 * Custom event cell for list display
 */
public class EventCell extends ListCell<EventI> {
    
    private EventData data;
    
    @Override
    public void updateItem(EventI event, boolean empty){
        super.updateItem(event, empty);
        if (event != null) {
            data = new EventData();
            data.init(event);
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
        @FXML private ImageView priorityImage;
        @FXML private Label name;
        
        private EventI event;

        public EventData() {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/EventCellFXML.fxml"));
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public void init(EventI ev) {
            event = ev;
            name.setText(event.getName());
            if (event.getPriority() != null) {
                Image priority = new Image(returnImageFilePath());
                priorityImage.setImage(priority);
            }
        }
        
        private String returnImageFilePath() {
            String path = "/gui/resources/";
            String priority = event.getPriority().toString();
            return path + priority.toLowerCase() + ".png";
        }
        
        public EventI getEvent() {
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