/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import gui.ApplicationControl;
import gui.CategoryCell;
import gui.DataHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import gui.EventCell;
import gui.ItemNotFoundException;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import model.CategoryI;
import model.Event;
import model.EventI;
import model.EventI.Priority;
import model.EventI.Repeat;

/**
 * FXML Controller class for Dashboard View
 *
 * @author anniefifer1
 */
public class DashboardViewFXMLController implements Initializable {

    ObservableList<EventI> eventList;
    ObservableList<CategoryI> categoryList;

    @FXML public ListView<EventI> eventListView;
    @FXML private ListView<CategoryI> categoryListView;
    @FXML Button deleteCategoryButton;
    @FXML Button editCategoryButton;
    @FXML ComboBox<Priority> priorityFilterOptions;
    @FXML Button priorityFilterButton;
    
    
    /**
     * Triggered when user elects to add an event
     * Opens dialog window for entering event information
     */
    @FXML
    protected void handleAddEventButtonAction(ActionEvent event) {
        try {
            ApplicationControl.getInstance().openFXMLWindow("/gui/fxml/AddEventFXML.fxml");
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
            ApplicationControl.getInstance().openFXMLWindow("/gui/fxml/AddCategoryFXML.fxml");
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
    
    /**
     * Triggered when user elects to edit a category
     * Opens dialog window for entering category information
     */
    @FXML
    protected void handleEditCategoryButtonAction(ActionEvent event) {
        try {
            ApplicationControl.getInstance().openFXMLWindow("/gui/fxml/EditCategoryFXML.fxml");
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
    
    /**
     * Triggered when user elects to delete a category
     */
    @FXML
    protected void handleDeleteCategoryButtonAction(ActionEvent event) {
        try {
            if (DataHandler.getInstance().getCurrentCategory().getName() == "default") {
                // tell user they can't delete default
                return;
            }
            CategoryI cat = DataHandler.getInstance().getCurrentCategory();
            DataHandler.getInstance().removeCategory(cat.getName());
            categoryListView.getItems().remove(cat);
        } catch (ItemNotFoundException e) {
            System.err.println("Error occured, category not selected");
        }
    }
    
    /**
     * Triggered when user elects to filter by priority
     */
    @FXML
    protected void handlePriorityFilterButtonAction() {
        Priority priority = priorityFilterOptions.getValue();
        ObservableList<EventI> filteredList = FXCollections.observableArrayList();
        for (Object e : eventListView.getItems().toArray()) {
            EventI event = (EventI) e;
            if (event.getPriority() == priority) {
                filteredList.add(event);
            }
        }
        eventListView.setItems(filteredList);
        priorityFilterButton.setText("Show all");
        priorityFilterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                handleShowAllButtonAction();
            }
        });
    }
    
    /**
     * Triggered when user elects to un-filter by priority
     */
    protected void handleShowAllButtonAction() {
        initEventBox();
        priorityFilterButton.setText("Filter");
        priorityFilterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                handlePriorityFilterButtonAction();
            }
        });
    }
    
    /**
     * Sets event cells for display
     */
    private void setEventListViewCellFactory() {
        eventListView.setCellFactory(new Callback<ListView<EventI>, javafx.scene.control.ListCell<EventI>>() {
            @Override
            public ListCell<EventI> call(ListView<EventI> listView) {
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
                EventI newEvent = (EventI) eventListView.getSelectionModel().getSelectedItem();
                if (newEvent != null) {
                    DataHandler.getInstance().setCurrentEvent(newEvent);
                    ApplicationControl.getInstance().openFXMLWindow("/gui/fxml/DisplayEventFXML.fxml");
                }
            }
        });
    }
    
    /**
     * Sets category cells for display
     */
    private void setCategoryListViewCellFactory() {
        categoryListView.setCellFactory(new Callback<ListView<CategoryI>, javafx.scene.control.ListCell<CategoryI>>() {
            @Override
            public ListCell<CategoryI> call(ListView<CategoryI> listView) {
                return new CategoryCell();
            }
        });
    }
    
    /**
     * Adds listener to filter events when user selects a category
     */
    private void addCategoryListViewListener() {
        categoryListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<CategoryI>() {
            @Override
            public void changed(ObservableValue<? extends CategoryI> ov, 
                    CategoryI oldCat, CategoryI newCat) {
                if (newCat != null) {
                    try {
                        DataHandler.getInstance().setCurrentCategory(newCat);
                        deleteCategoryButton.setVisible(true);
                        editCategoryButton.setVisible(true);
                    } catch(Exception e) {
                       e.printStackTrace();
                    }                                    
                }
            }
        });
    }
    
    /**
     * Forces updates of EventCells in eventListView
     */
    public void resetEventListView() {
        
        CategoryI c = DataHandler.getInstance().getCategories().get(0);
        EventI e = new Event(" ", null, null, false, Repeat.NONE, c);
        DataHandler.getInstance().addEvent(e);
        DataHandler.getInstance().removeEvent(e);
    }
    
    /**
     * Forces updates of EventCells in eventListView
     */
    public void resetCategoryListView() {
        categoryListView.setItems(null);
        initCategoryBox();
    }
    
    /**
     * Initializes priority filter choices
     */
    private void initPriorityFilter() {
        priorityFilterOptions.setItems(FXCollections.observableArrayList(Priority.values()));
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
        editCategoryButton.setVisible(false);
        initEventBox();
        initCategoryBox();
        initPriorityFilter();
    }
    
}
