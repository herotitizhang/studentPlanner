/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.application.Application;
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
        ApplicationControl.getInstance().initiateLogin();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
