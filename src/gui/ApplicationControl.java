/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.controllers.AddEventFXMLController;
import gui.controllers.LoginFXMLController;
import gui.controllers.SimpleDialogFXMLController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author anniefifer1
 */
public class ApplicationControl {
    
    private final static ApplicationControl instance = new ApplicationControl();
    
    public boolean onlineMode = true;
    
    public ApplicationControl() {
        
    }
    
    public static ApplicationControl getInstance() {
        return instance;
    }
    
    public void setOnlineMode(boolean mode) {
        onlineMode = mode;
    }
    
    public void startApplication() {
        if (onlineMode) {
            // open online version
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/gui/fxml/MainFXML.fxml"));
                /* These will be moved to local stylesheets */
                Scene scene = new Scene(root);
                scene.getStylesheets().addAll("http://fonts.googleapis.com/css?family=Slabo+27px",
                        "http://fonts.googleapis.com/css?family=Oswald",
                        "http://fonts.googleapis.com/css?family=Pontano+Sans");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // open read-only version
        }
    }
    
    public void refreshData() {
        
    }
    
    public void openSimpleDialog(String msg) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/SimpleDialogFXML.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene((Parent) loader.load()));
            SimpleDialogFXMLController controller = loader.<SimpleDialogFXMLController>getController();
            controller.setMessage(msg);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AddEventFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void closeWindow(Node node) {
        Scene scene = node.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }
    
}
