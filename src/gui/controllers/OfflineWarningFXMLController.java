/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import gui.ApplicationControl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author anniefifer1
 */
public class OfflineWarningFXMLController implements Initializable {

    @FXML Button closeButton;
    
    @FXML
    protected void handleCloseButtonAction() {
        ApplicationControl.getInstance().closeWindow(closeButton);
    }
    
    @FXML
    protected void handleOfflineButtonAction() {
        ApplicationControl.getInstance().loadApplicationOffline();
        ApplicationControl.getInstance().closeWindow(closeButton);
        LoginFXMLController c = (LoginFXMLController) ApplicationControl.getInstance().
                getController("/gui/fxml/LoginFXML.fxml");
        c.closeWindow();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
