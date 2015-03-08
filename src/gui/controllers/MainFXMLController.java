/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

/**
 *
 * @author anniefifer1
 */
public class MainFXMLController implements Initializable {
   
    @FXML
    protected void updateSchedule() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/ScheduleViewFXML.fxml"));
            Parent p = fxmlLoader.load();
            ScheduleViewFXMLController controller = fxmlLoader.<ScheduleViewFXMLController>getController();
            controller.initializeSchedule();
        } catch (IOException ex) {
            Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
