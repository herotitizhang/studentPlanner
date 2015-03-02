/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import gui.CategoryCell;
import gui.DataHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Event;
import gui.EventCell;
import gui.ItemNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import model.Category;
import model.EventI;

/**
 * FXML Controller class for Dashboard View
 *
 * @author anniefifer1
 */
public class DashboardViewFXMLController implements Initializable {

    ObservableList<Event> eventList;
    ObservableList<Category> categoryList;

    @FXML private ListView eventListView;
    @FXML private ListView categoryListView;
    @FXML Button deleteCategoryButton;
    
    /**
     * Triggered when user elects to add an event
     * Opens dialog window for entering event information
     */
    @FXML
    protected void handleAddEventButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/AddEventFXML.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
    
    /**
     * Triggered when user elects to add a category
     * Opens dialog window for entering category information
     */
    @FXML
    protected void handleAddCategoryButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/AddCategoryFXML.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
    
    @FXML
    protected void handleDeleteCategoryButtonAction(ActionEvent event) {
        try {
            DataHandler.getInstance().deleteCurrentCategory();
        } catch (ItemNotFoundException e) {
            System.err.println("Error occured, category not selected");
        }
    }
    
    /**
     * Sets event cells for display
     */
    private void setEventListViewCellFactory() {
        eventListView.setCellFactory(new Callback<ListView<Event>, javafx.scene.control.ListCell<Event>>() {
            @Override
            public ListCell<Event> call(ListView<Event> listView) {
                return new EventCell();
            }
        });
    }
    
    /**
     * Adds listener to open information window when user selects an event
     */
    private void addEventListViewClickHandler() {
        eventListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    EventI newEvent = (Event) eventListView.getSelectionModel().getSelectedItem();
                    DataHandler.getInstance().setCurrentEvent(newEvent);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/DisplayEventFXML.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(DashboardViewFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    /**
     * Sets category cells for display
     */
    private void setCategoryListViewCellFactory() {
        categoryListView.setCellFactory(new Callback<ListView<Category>, javafx.scene.control.ListCell<Category>>() {
            @Override
            public ListCell<Category> call(ListView<Category> listView) {
                return new CategoryCell();
            }
        });
    }
    
    /**
     * Adds listener to filter events when user selects a category
     */
    private void addCategoryListViewListener() {
        categoryListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Category>() {
            @Override
            public void changed(ObservableValue<? extends Category> ov, 
                    Category oldCat, Category newCat) {
                if (newCat != null) {
                    try {
                        DataHandler.getInstance().setCurrentCategory(newCat);
                        deleteCategoryButton.setVisible(true);
                    } catch(Exception e) {
                       e.printStackTrace();
                    }                                    
                }
            }
        });
    }
    
    public void resetEventListView() {
        try {
            eventListView.getItems().remove(DataHandler.getInstance().getCurrentEvent());
            eventListView.setItems(null);
            eventListView.setItems(DataHandler.getInstance().getEventList());
        } catch (ItemNotFoundException ex) {
            Logger.getLogger(DashboardViewFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    /**
     * Initializes list of events
     */
    private void initEventBox() {
        eventListView.setItems(DataHandler.getInstance().getEventList());
        setEventListViewCellFactory();
        addEventListViewClickHandler();
    }
    
    /**
     * Initializes list of categories
     */
    private void initCategoryBox() {
        categoryListView.setItems(DataHandler.getInstance().getCategories());
        setCategoryListViewCellFactory();
        addCategoryListViewListener();
    }
    
    /**
     * Initializes event content and category content
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        deleteCategoryButton.setVisible(false);
        initEventBox();
        initCategoryBox();
    }
    
}
