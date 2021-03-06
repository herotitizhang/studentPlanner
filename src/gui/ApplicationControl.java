/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import backendIO.ClientIOSystem;
import backendIO.ClientRequest;
import backendIO.IPValidator;
import backendIO.ServerCommunicator;
import backendIO.ServerResponse;
import gui.controllers.AddEventFXMLController;
import gui.controllers.DashboardViewFXMLController;
import gui.controllers.LoginFXMLController;
import gui.controllers.SimpleDialogFXMLController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.EventI;
import model.Schedule;
import model.ScheduleI;

/**
 *
 * @author anniefifer1
 */
public class ApplicationControl {
    
    private final static ApplicationControl instance = new ApplicationControl();
    List<EventI> alertEvents = new ArrayList<EventI>();
        
    public ApplicationControl() {
        
    }
    
    public static ApplicationControl getInstance() {
        return instance;
    }
    
    /**
     * Opens login window if connection is available
     * @throws IOException 
     */
    public void initiateLogin() throws IOException {
        openFXMLWindow("/gui/fxml/LoginFXML.fxml");
    }
    
    /**
     * Opens application without checking for server connection or responses
     */
    public void loadApplicationOffline() {
        DataHandler.getInstance().setSchedule(new Schedule());
        openMainWindow();
    }
        
    /**
     * Opens application, informs user of server connection issues
     */
    public void loadApplication() {
        if (!ServerCommunicator.checkConnection()) {
            openSimpleDialog("Couldn't connect to server! Please open a file instead.");
        } 
        if (!ServerCommunicator.isLoggedIn()) {
            // Not sure this will happen
        } 

        try {
            ServerResponse serverResponse = ServerCommunicator.sendClientRequest(ServerCommunicator.generateLoadRequest());
            if (serverResponse == null) {
                openMainWindow();
                openSimpleDialog("No response from server! Please open a file instead.");
            }
            if (serverResponse.isAccepted()) {
                ScheduleI schedule;
                if (serverResponse.getSchedule() != null) {
                    schedule = (Schedule)ClientIOSystem.getObject(serverResponse.getSchedule());
                } else {
                    schedule = new Schedule();
                }
                DataHandler.getInstance().setSchedule(schedule);
                openMainWindow();
            } else {
                openMainWindow();
                openSimpleDialog("Server rejected request! Please open a file instead.");
            }
        } catch (IOException e) {
            openMainWindow();
            openSimpleDialog("No internet connection, please open a file instead.");
        }
    }
    
    /**
     * Opens the main window of the program
     */
    public void openMainWindow() {
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
    }
    
    /**
     * Opens FXML file at provided file path, returns the corresponding controller
     * @param filePath
     * @return 
     */
    public Object openFXMLWindow(String filePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filePath));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
            Object controller = loader.getController();
            return controller;
        } catch (IOException ex) {
            Logger.getLogger(ApplicationControl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Returns controller for FXML file at provided file path
     * @param filePath
     * @return 
     */
    public Object getController(String filePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filePath));
            Parent root = (Parent) loader.load();
            return loader.getController();
        } catch (IOException ex) {
            Logger.getLogger(ApplicationControl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Reloads event and category content displayed in application
     */
    public void refreshDisplayContent() {
        DashboardViewFXMLController dashControl = (DashboardViewFXMLController) getController(
                "/gui/fxml/DashboardViewFXML.fxml");
        dashControl.resetCategoryListView();
        dashControl.resetEventListView();
        // TO DO: Also refresh schedule view
    }
    
    /**
     * Opens a dialog window with "Ok" button that displays message
     * @param msg 
     */
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
    
    /**
     * Can be called by any controller to close its window when passed any node in that window
     * @param node 
     */
    public void closeWindow(Node node) {
        Scene scene = node.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

    /**
     * Sets the IP of ServerCommunicator if provided IP is valid
     * @param ip
     * @return 
     */
    public static boolean setIP(String ip) { // TODO handle names that include space
        if (!IPValidator.validate(ip)) {
            return false;
        }
        ServerCommunicator.setServerIP(ip);
        return true;
    }
    
    /**
     * Attempts to save to either both server and disk, if saving to server fails
     * then nothing is done and informs the user that schedule cannot be saved
     */
    public void save() {
        
        if (!ServerCommunicator.checkConnection()) {
            openSimpleDialog("Sorry no connection is available, cannot save.");
        }

        if (!ServerCommunicator.isLoggedIn()) {
            // Not sure this will happen
        } 

        try {
            
            ServerResponse serverResponse = ServerCommunicator.sendClientRequest(
                    ServerCommunicator.generateSaveRequest(DataHandler.getInstance().getSchedule()));
            
            if (serverResponse == null) {
                openSimpleDialog("No response from the server! You schedule is not saved.");
            }
            if (serverResponse.isAccepted()) {
                ClientIOSystem.writeToDisk(DataHandler.getInstance().getSchedule(), ServerCommunicator.getUsername());
                for (EventI event: alertEvents) {
                    requestAlert(event);
                }
                alertEvents.clear();
            } else {
                openSimpleDialog("Sorry, server was not able to save your schedule");
            }
            System.out.println();
            
        } catch (IOException e) {
            openSimpleDialog("Sorry, can't connect to the internet!");
        }
        
    }

    /**
     * Loads and sets schedule, loads schedule content in display
     * @param fileName 
     */
    public void loadSchedule(String fileName) {
        ScheduleI tempSchedule = ClientIOSystem.loadFromDisk(fileName);
        if (tempSchedule != null) {
            DataHandler.getInstance().setSchedule(tempSchedule);
            refreshDisplayContent();
	}
    }

    /**
     * Attempts to log in user with username and password
     * @param username
     * @param password
     * @return true if log in is successful 
     */
    public boolean LogInUser(String username, String password) {

        try {
            ServerResponse serverResponse = ServerCommunicator.sendClientRequest(
                    ServerCommunicator.generateLoginRequest(username, password));
            if (serverResponse.isAccepted()) { 
                return true;
            } else if (serverResponse == null) {
                ApplicationControl.getInstance().openSimpleDialog("No response from server.");
            } else {
                ApplicationControl.getInstance().openSimpleDialog("Server rejected input.");
            }
        } catch (IOException ex) {
            ApplicationControl.getInstance().openSimpleDialog("Problem connecting to internet.");
            Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * Creates new account and logs in user if creation is successful. Warns user if 
     * no phone is provided
     * @param username
     * @param password
     * @param phone
     * @return account creation and log in successful
     */
    public boolean CreateAndLogInUser(String username, String password, String phone) {
        if (!ServerCommunicator.checkConnection()) {
            return false;
        }
        if (ServerCommunicator.isLoggedIn()) {
            ApplicationControl.getInstance().openSimpleDialog("You are already logged in, can't create new account");
        } else {
            try {
                
                ServerResponse serverResponse = ServerCommunicator.sendClientRequest(
                        ServerCommunicator.generateCreateRequest(username, password));
                if (serverResponse == null) {
                    ApplicationControl.getInstance().openSimpleDialog("No response from server.");
                } else if (serverResponse.isAccepted()) {
                    if (!phone.isEmpty()) {
                        if (requestPhoneAuthentication(phone)) {
                            openSimpleDialog("You will receive an authentication text soon.");
                        } else {
                            openSimpleDialog("There was an issue setting your phone number. \n"
                                    + "Please resolve this in your account settings.");
                        }
                    } else {
                        openSimpleDialog("Warning: without a phone number, you will not recieve alerts");
                    }
                    return true;
                } else {
                    ApplicationControl.getInstance().openSimpleDialog("Server rejected: "+serverResponse.getFailureNotice());
                }
                
            } catch (IOException e) {
                ApplicationControl.getInstance().openSimpleDialog("Problem connecting to internet.");
            }
        }
        return false;
    }
    
    /**
     * Requests authentication for phone number
     * @param number
     * @return 
     */
    public boolean requestPhoneAuthentication(String number) {
        ServerCommunicator.setAuthenticated(false);
	boolean processed = false;
        
        try {
            ServerResponse serverResponse = ServerCommunicator.sendClientRequest(ServerCommunicator.generateRequestAuthRequest(number));
            if (serverResponse.isAccepted()) {
                ServerCommunicator.setPhoneNumber(number);
                processed = true;
            }
        } catch (IOException e) {
            //
        }
        return processed;
    }
    
    /**
     * Requests verification for code
     * @param code
     * @return 
     */
    public boolean authenticatePhone(String code) {
        try {
            ServerResponse serverResponse = ServerCommunicator.sendClientRequest(ServerCommunicator.generateAuthenticateRequest(code));
            if (serverResponse == null) {
                openSimpleDialog("No response from the server!");
                return false;
            }
            if (serverResponse.isAccepted()) {
                ServerCommunicator.setAuthenticated(true);
                openSimpleDialog("Your phone number has been authenticated. You can get alerts from now on.");
                return true;
            } else {
                openSimpleDialog("Code was rejected by the server.");
                return false;
            }
        } catch (IOException e) {
            openSimpleDialog("Error: cannot connect to the Internet!");
            return false;
        }
    }
    
    public void addAlertEvent(EventI event) {
        alertEvents.add(event);
    }
    
    /**
     * Requests an alert for provided event
     * @param event 
     */
    public void requestAlert(EventI event) {
        String alertText = event.getAlertText();
        GregorianCalendar alertTime = event.getAlertTime();
        
        try {
            ClientRequest alertRequest = ServerCommunicator.generateAlertRequest
                    (ServerCommunicator.getUsername()+"."+event.getCategory()+"."+event.getName(), event.getAlertText(), event.getRepeating().toString(), event.getAlertTime());
            ServerResponse serverResponse = ServerCommunicator.sendClientRequest(alertRequest);
            if ((serverResponse == null) || (!serverResponse.isAccepted())) {
                openSimpleDialog("Sorry, couldn't request an alert.");
            }
        } catch (IOException e) {
            openSimpleDialog("Couldn't connect to the internet, alert not processed");
        }
    }
    
}
