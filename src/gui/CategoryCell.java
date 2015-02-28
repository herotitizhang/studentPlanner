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
import model.Category;

/**
 * Custom category cell for list display
 */
public class CategoryCell extends ListCell<Category> {
    
    private CategoryData data;
    
    @Override
    public void updateItem(Category category, boolean empty){
        super.updateItem(category, empty);
        if (category != null) {
            data = new CategoryData();
            data.setCategory(category);
            setGraphic(data.getBox());
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
        
        private Category category;

        public CategoryData() {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/CategoryCellFXML.fxml"));
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void setCategory(Category cat){
           name.setText(cat.toString());
        }
        
        public Category getCategory() {
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