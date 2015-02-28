/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main program for GUI application
 * @author anniefifer1
 */
public class StudentPlanner extends Application {
    
    /**
     * Loads and displays main FXML window
     * @param stage
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("fxml/MainFXML.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll("http://fonts.googleapis.com/css?family=Slabo+27px", 
                "http://fonts.googleapis.com/css?family=Oswald", 
                "http://fonts.googleapis.com/css?family=Pontano+Sans");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
