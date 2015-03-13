/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.controllers.ConfirmDeleteFXMLController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
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
    
    private HBox hBox = new HBox();
    private ImageView priorityImage = new ImageView();
    private Label name = new Label();
    private CheckBox completed = new CheckBox();
    private EventI event;
    
    StringProperty nameProperty = new SimpleStringProperty();
    
    public EventCell() {
        super();
        hBox.getChildren().addAll(priorityImage, name, completed);
        hBox.setSpacing(10);
        completed.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleCompletedChecked();
            }
        });
        name.textProperty().bind(nameProperty);
    }
    
    private void handleCompletedChecked() {
        ConfirmDeleteFXMLController c = (ConfirmDeleteFXMLController) ApplicationControl.
                getInstance().openFXMLWindow("/gui/fxml/ConfirmDeleteFXML.fxml");
        c.setEvent(event);
    }
    
    @Override
    public void updateItem(EventI ev, boolean empty){
        super.updateItem(ev, empty);
        if (ev != null) {
            event = ev;
            updateGraphic();
            setGraphic(getBox());
        } else {
            setGraphic(null);
            setText(null);
        }
    }
    
    public void updateGraphic() {
        nameProperty.setValue(event.getName());
        if (event.getPriority() != null) {
            Image priority = new Image(returnImageFilePath());
            priorityImage.setImage(priority);
        }
        completed.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                handleCompletedChecked();
            }
        });
        completed.setSelected(false);
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