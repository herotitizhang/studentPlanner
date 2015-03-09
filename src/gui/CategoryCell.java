/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import model.CategoryI;

/**
 * Custom category cell for list display
 */
public class CategoryCell extends ListCell<CategoryI> {
    
    private CategoryData data;
    
    @Override
    public void updateItem(CategoryI category, boolean empty){
        super.updateItem(category, empty);
        if (category != null) {
            data = new CategoryData();
            data.setCategory(category);
            setGraphic(data.getBox());
        } else {
            setGraphic(null);
            setText(null);
        }
    }
    
    public CategoryData getCategoryData() {
        return data;
    }
    
    /**
     * Class that stores category data for access by CategoryCell
     */
    public class CategoryData {

        @FXML private HBox hBox;
        @FXML private Label name;
        private StringProperty nameProperty = new SimpleStringProperty();
        
        private CategoryI category;

        public CategoryData() {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/CategoryCellFXML.fxml"));
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            name.textProperty().bind(nameProperty);
        }

        public void setCategory(CategoryI cat){
           nameProperty.setValue(cat.toString());
        }
        
        public CategoryI getCategory() {
            return category;
        }

        /**
         * @return HBox object with category information
         */
        public HBox getBox() {
            return hBox;
        }
    }
    
}