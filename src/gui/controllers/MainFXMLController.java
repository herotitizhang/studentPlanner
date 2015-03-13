/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;
import gui.ApplicationControl;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author anniefifer1
 */
public class MainFXMLController implements Initializable {
   
    @FXML
    protected void handleSaveAction() {
        ApplicationControl.getInstance().save();
    }
    
    @FXML
    protected void handleOpenAction() {
        String currentDir = System.getProperty("user.dir") + File.separator;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        fileChooser.setInitialDirectory(new File(currentDir));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SAV Files", "*.sav"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            ApplicationControl.getInstance().loadSchedule(selectedFile.getName());
        }
    }
    
    @FXML
    protected void handleSettingsAction() {
        // to do
        ApplicationControl.getInstance().openFXMLWindow("/gui/fxml/SettingsFXML.fxml");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
