package console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import backendIO.ClientIOSystem;
import backendIO.ClientRequest;
import backendIO.ServerCommunicator;
import backendIO.ServerResponse;
import model.CategoryI;
import model.Event;
import model.EventI;
import model.NameInUseException;
import model.Schedule;
import model.EventI.Repeat;
import model.EventI.Priority;
import model.ScheduleI;

public class ConsoleDriver {

	private static Schedule schedule = new Schedule();
	private static Scanner console = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("Enter \"help\" to see a list of available commands");
		if (!ServerCommunicator.checkConnection()) {
			System.out.println("Note: You are currently not connected to the internet.");
			System.out.println("All the changes you make cannot be saved.");
			System.out.println("However, You are able to load from local drive.");
			System.out.println("Enter \"check_inet\" to see if the client side is connected to the server now.");
			System.out.println();
		} else {
			System.out.println("Internet is available. We suggest you login first before creating events.");
		}
		while (console.hasNextLine()){
			String userInput = console.nextLine();
			processUserInput(userInput);
		}
	}
	
	private static void processUserInput(String userInput) {
		if (userInput.startsWith("add_ctgr")){
			addCategory(userInput);
		} else if (userInput.startsWith("rm_ctgr")){
			removeCategory(userInput);
		} else if (userInput.startsWith("rename_ctgr")) { 
			editCategory(userInput);
		} else if (userInput.equals("list_ctgrs")) {
			listCategories();
		} else if (userInput.equals("create_event")){ 
			createEvent();
		} else if (userInput.startsWith("edit_event")) { 
			editEvent(userInput);
		} else if (userInput.startsWith("list_events_using_ctgr")) {
			listEventsBasedOnCategory(userInput);
		} else if (userInput.startsWith("list_events_using_priority")){
			listEventsBasedOnPriority(userInput);
		} else if (userInput.startsWith("list_events_using_times")){
			listEventsBasedOnTimeFrame(userInput); 
		} else if (userInput.equals("check_inet")){
			checkInternet();
		} else if (userInput.equals("save")){
			save();
		} else if (userInput.equals("load")){
			load();
		} else if (userInput.startsWith("create")){
			create(userInput);
		} else if (userInput.startsWith("login")){
			login(userInput);
		} else if (userInput.startsWith("request_auth")){
			requestAuthenticate(userInput);
		} else if (userInput.startsWith("auth")){
			authenticate(userInput);
		} else if (userInput.startsWith("request_alert")){		
			requestAlert(userInput);
		} else if (userInput.equals("exit") || userInput.equals("quit")) { 
			exit();
		} else {
			printHelp();
		} 
	} 
	
	private static void addCategory(String userInput) {
		String[] tokens = userInput.split("\\s+");
		if (tokens.length == 2) {
			try {
				schedule.addCategory(tokens[1]);
				System.out.println("The category named \""+tokens[1]+"\" has been added successfully!");
			} catch (NameInUseException e) {
				System.out.println("The name "+ e.getName()+" has been used!");
//				e.printStackTrace();
			} finally {
				System.out.println();
			}
		} else {
			printHelp();
		}
	}
	
	private static void removeCategory(String userInput) {
		String[] tokens = userInput.split("\\s+");
		if (tokens.length == 2) {
			if (schedule.removeCategory(tokens[1]) != null) {
				System.out.println("The category named \""+tokens[1]+"\" has been removed successfully!");
			} else {
				System.out.println("The category was not removed.");
			}
			System.out.println();
		} else {
			printHelp();
		}
	}
	
	private static void editCategory(String userInput) {
		String[] tokens = userInput.split("\\s+");
		if (tokens.length == 3) {
			if (schedule.editCategoryName(tokens[1], tokens[2]) ){
				System.out.println("The "+tokens[1]+" category has been renamed! All events now belong to "+tokens[2]+" category.");
			} else {
				System.out.println("The category named "+tokens[1]+" does not exist.");
			}
			System.out.println();
		} else {
			printHelp();
		}
	}
	
	
	private static void listCategories() {
		for (CategoryI category: schedule.getCategories()) {
			System.out.println(category);
		}
		System.out.println();
	}
	
	private static void createEvent() {
		// get name
		System.out.println("Please enter the event name.");
		String name = console.nextLine();

		// get text
		System.out.println("Please enter the description of the event.");
		String text = console.nextLine();

		// get startTime and endTime
		GregorianCalendar startTime = null, endTime = null;
		String temp = "";
		String[] tokens = null;
		do {
			System.out
					.println("Please enter the start time in such a format yyyy-mm-dd-hh-mm.");
			temp = console.nextLine();
			tokens = temp.trim().split("-");
			while (tokens.length != 5) { // TODO
				System.out.println("Invalid format. Enter it again.");
				temp = console.nextLine();
				tokens = temp.trim().split("-");
			}
			startTime = new GregorianCalendar(Integer.parseInt(tokens[0]),
					Integer.parseInt(tokens[1]) - 1,
					Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]),
					Integer.parseInt(tokens[4]));

			// get endTime
			System.out
					.println("Please enter the end time in such a format yyyy-mm-dd-hh-mm.");
			temp = console.nextLine();
			tokens = temp.trim().split("-");
			while (tokens.length != 5) { // TODO
				System.out.println("Invalid format. Enter it again.");
				temp = console.nextLine();
				tokens = temp.trim().split("-");
			}
			endTime = new GregorianCalendar(Integer.parseInt(tokens[0]),
					Integer.parseInt(tokens[1]) - 1,
					Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]),
					Integer.parseInt(tokens[4]));

			if (startTime.compareTo(endTime) <= 0) { // qualified startTime and
														// endTime
				break;
			} else {
				System.out
						.println("Invalid times: end time is before starting time!");
			}
		} while (true);

		// get alert
		System.out.println("Do you want to add the alert? [Y/N]");
		boolean alert = false;
		temp = console.nextLine();
		if (temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("y"))
			alert = true;

		String alertText = null;
		GregorianCalendar alertTime = null;
		if (alert) {
			// get alertText
			System.out.println("Please enter the description of the alert.");
			alertText = console.nextLine();

			// get alertTime
			System.out
					.println("Please enter the alert time in such a format yyyy-mm-dd-hh-mm.");
			temp = console.nextLine();
			tokens = temp.trim().split("-");
			while (tokens.length != 5) {
				System.out.println("Invalid format. Enter it again.");
				temp = console.nextLine();
				tokens = temp.trim().split("-");
			}
			alertTime = new GregorianCalendar(Integer.parseInt(tokens[0]),
					Integer.parseInt(tokens[1]) - 1,
					Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]),
					Integer.parseInt(tokens[4]));
		}

		// get repeat NONE, WEEKLY, MONTHLY, YEARLY;
		System.out.println("Do you want it to repeat weekly, monthly, yearly or does not repeat? [W/M/Y/N]");
		Repeat repeat = Repeat.NONE;
		temp = console.nextLine();
		if (temp.equalsIgnoreCase("weekly") || temp.equalsIgnoreCase("w"))
			repeat = Repeat.WEEKLY;
		else if (temp.equalsIgnoreCase("monthly") || temp.equalsIgnoreCase("m"))
			repeat = Repeat.MONTHLY;
		else if (temp.equalsIgnoreCase("yearly") || temp.equalsIgnoreCase("y"))
			repeat = Repeat.YEARLY;

		// get priority LOW, MEDIUM, HIGH, URGENT;
		System.out.println("What is the priority of the event (low, medium, high or urgent) ? [L/M/H/U]");
		EventI.Priority priority = EventI.Priority.MEDIUM;
		temp = console.nextLine();
		if (temp.equalsIgnoreCase("low") || temp.equalsIgnoreCase("l"))
			priority = Priority.LOW;
		else if (temp.equalsIgnoreCase("high") || temp.equalsIgnoreCase("h"))
			priority = Priority.HIGH;
		else if (temp.equalsIgnoreCase("urgent") || temp.equalsIgnoreCase("u"))
			priority = Priority.URGENT;

		// get category
		System.out
				.println("What category is it? Below are existing categories:");
		for (CategoryI category : schedule.getCategories())
			System.out.print(category + " ");
		System.out.println();
		CategoryI category = null;
		temp = console.nextLine();
		if (temp.trim().length() == 0) {
			System.out
					.println("You didn't enter the category, so the event belongs to the default category.");
			category = schedule.getCategoriesMap().get("default");
		} else if (schedule.getCategoriesMap().containsKey(temp)) {
			category = schedule.getCategoriesMap().get(temp);
		} else {
			try {
				category = schedule.addCategory(temp);
			} catch (NameInUseException e) {
				System.out.println("The name " + e.getName() + " is used!");
				e.printStackTrace();
			}
		}

		new Event(name, text, startTime, endTime, alert, alertText, alertTime,
				repeat, priority, category); // the constructor will add the
												// event to the category
		System.out.println("An event named \"" + name + "\" has been created!");
		System.out.println();
	}
	
	private static void editEvent(String userInput) {
		if (userInput.length() < 11 // edit_event plus a space after it
				|| userInput.charAt(10) != ' ') {
			printHelp();
		} else {
			String eventName = userInput.substring(11);

			boolean foundEvent = false;
			for (EventI event: schedule.getAllSortedEvents()) {
				if (event.getName().equals(eventName)) {
					foundEvent = true;

					boolean setAnotherAttr = false;
					do {

						System.out.println("The current event has the following settings.");
						System.out.println(event);
						System.out.println();
						System.out.println("Which field do you want to edit? Select from the following:");
						System.out.println("name, text, start time, end time, alert, alert text, alert time, repeat, priority and category");

						String command = console.nextLine().trim();
						while (!command.equals("name") && !command.equals("text") &&
								!command.equals("start time") && !command.equals("end time") && 
								!command.equals("alert") && !command.equals("alert text") &&
								!command.equals("alert time") && !command.equals("repeat") && 
								!command.equals("category") ) {
							System.out.println("Invalid input; enter the field again");
							command = console.nextLine().trim();
						}

						if (command.equals("name")) {
							System.out.println("Please enter the new name:");
							event.setName(console.nextLine());
						} else if (command.equals("text")) {
							System.out.println("Please enter the new text:");
							event.setText(console.nextLine());
						} else if (command.equals("start time")) {
							GregorianCalendar startTime = null;
							while (true) {
								System.out.println("Please enter the new start time in such a format yyyy-mm-dd-hh-mm.");
								String temp = console.nextLine();
								String[] timeTokens = temp.trim().split("-");
								while (timeTokens.length != 5) { // TODO replace it with validation
									System.out.println("Invalid format. Enter it again.");
									temp = console.nextLine();
									timeTokens = temp.trim().split("-");
								}
								startTime = new GregorianCalendar(Integer.parseInt(timeTokens[0]),
										Integer.parseInt(timeTokens[1])-1, Integer.parseInt(timeTokens[2]), 
										Integer.parseInt(timeTokens[3]), Integer.parseInt(timeTokens[4]));
								if (startTime.compareTo(event.getEndTime()) <= 0) {
									break;
								} else {
									System.out.println("Invalid times: end time is before starting time!");
								}
							}
							
							event.setStartTime(startTime);
						} else if (command.equals("end time")) {
							GregorianCalendar endTime = null;
							while (true) {
								System.out.println("Please enter the end time in such a format yyyy-mm-dd-hh-mm.");
								String temp = console.nextLine();
								String[] timeTokens = temp.trim().split("-");
								while (timeTokens.length != 5) { //TODO
									System.out.println("Invalid format. Enter it again.");
									temp = console.nextLine();
									timeTokens = temp.trim().split("-");
								}
								endTime = new GregorianCalendar(Integer.parseInt(timeTokens[0]),
										Integer.parseInt(timeTokens[1])-1, Integer.parseInt(timeTokens[2]), 
										Integer.parseInt(timeTokens[3]), Integer.parseInt(timeTokens[4]));

								if (event.getStartTime().compareTo(endTime) <= 0) {
									break;
								} else {
									System.out.println("Invalid times: end time is before starting time!");
								}
							}
							event.setEndTime(endTime);

						} else if (command.equals("alert")) {
							System.out.println("Do you want to turn the alert "+ (event.hasAlert() ? "off" : "on") + " [Y/N]");
							String selection = console.nextLine();
							if (selection.equalsIgnoreCase("yes") || selection.equalsIgnoreCase("y")) 
								event.setAlert(!event.hasAlert());
							System.out.println("Note: you need to request the alert one more time in order to make the change take effect.");
						} else if (command.equals("alert text")) {
							if (event.hasAlert()) {
								System.out.println("Please enter the new alert text:");
								event.setAlertText(console.nextLine());
								System.out.println("Note: you need to request the alert one more time in order to make it the change take effect.");
							} else {
								System.out.println("Please turn on the alert before you want to set the alert text.");
							}
						} else if (command.equals("alert time")) {
							if (event.hasAlert()) {
								System.out.println("Please enter the alert time in such a format yyyy-mm-dd-hh-mm.");
								String temp = console.nextLine();
								String[] timeTokens = temp.trim().split("-");
								while (timeTokens.length != 5) { //TODO
									System.out.println("Invalid format. Enter it again.");
									temp = console.nextLine();
									timeTokens = temp.trim().split("-");
								}
								GregorianCalendar alertTime = new GregorianCalendar(Integer.parseInt(timeTokens[0]),
										Integer.parseInt(timeTokens[1])-1, Integer.parseInt(timeTokens[2]), 
										Integer.parseInt(timeTokens[3]), Integer.parseInt(timeTokens[4]));
								event.setAlertTime(alertTime);	
								System.out.println("Note: you need to request the alert one more time in order to make it the change take effect.");
							} else {
								System.out.println("Please turn on the alert before you want to set the alert time.");
							}
						} else if (command.equals("repeat")) {
							System.out.println("Do you want it to repeat weekly, monthly, yearly or does not repeat? [W/M/Y/N]");
							String temp = console.nextLine();
							if (temp.equalsIgnoreCase("weekly") || temp.equalsIgnoreCase("w"))
								event.setRepeating(Repeat.WEEKLY);
							else if (temp.equalsIgnoreCase("monthly") || temp.equalsIgnoreCase("m"))
								event.setRepeating(Repeat.MONTHLY);
							else if (temp.equalsIgnoreCase("yearly") || temp.equalsIgnoreCase("y"))
								event.setRepeating(Repeat.YEARLY);
							else 
								event.setRepeating(Repeat.NONE);

						} else if (command.equals("priority")) {
							// get priority LOW, MEDIUM, HIGH, URGENT;
							System.out.println("What is the priority of the event (low, medium, high or urgent) ? [L/M/H/U]");
							String temp = console.nextLine();
							if (temp.equalsIgnoreCase("low") || temp.equalsIgnoreCase("l"))
								event.setPriority(Priority.LOW);
							else if (temp.equalsIgnoreCase("high") || temp.equalsIgnoreCase("h"))
								event.setPriority(Priority.HIGH);
							else if (temp.equalsIgnoreCase("urgent") || temp.equalsIgnoreCase("u"))
								event.setPriority(Priority.URGENT);
							else 
								event.setPriority(Priority.MEDIUM);
						
						} else if (command.equals("category")) {
							System.out.println("Please enter the category. Below are existing categories:");
							for (CategoryI category: schedule.getCategories()) 
								System.out.print(category+" ");
							String temp = console.nextLine().trim();
							CategoryI category = null;
							if (temp.trim().length() == 0) { // default category
								System.out.println("You didn't enter the category, so the event belongs to the default category.");
								category = schedule.getCategoriesMap().get("default");
							} else if (schedule.getCategoriesMap().containsKey(temp)){ // existing category
								category = schedule.getCategoriesMap().get(temp);
							} else { // new category
								try {
									category = schedule.addCategory(temp);
								} catch (NameInUseException e) {
									System.out.println("The name "+ e.getName()+" has been used!");
								}
							}
							event.setCategory(category);
						}

						System.out.println("Would you like to set another field? [Y/N]");
						String selection = console.nextLine();
						if (selection.equalsIgnoreCase("yes") || selection.equalsIgnoreCase("y")) {
							setAnotherAttr = true;
						} else {
							setAnotherAttr = false;
						}

					} while (setAnotherAttr);

					break;
				}
			}
			if (!foundEvent) {
				System.out.println("The event does not exist!");
			}
			System.out.println();
		}
	}
	
	private static void listEventsBasedOnCategory(String userInput) {
		String[] tokens = userInput.split("\\s+");
		if (tokens.length == 2) {
			List<EventI> events = schedule.getEvents(tokens[1]);
			if (events != null) {
				for (EventI event: events) {
					System.out.println(event);
				}
			} else {
				System.out.println("Can't find the category!");
			}
			
			System.out.println();
		} else {
			printHelp();
		}
	}
	
	private static void listEventsBasedOnPriority(String userInput) {
		String[] tokens = userInput.split("\\s+");
		if (tokens.length == 2) {
			Priority priority = Priority.MEDIUM; // default
			if (tokens[1].equalsIgnoreCase("low") || tokens[1].equalsIgnoreCase("l"))
				priority = Priority.LOW;
			else if (tokens[1].equalsIgnoreCase("high") || tokens[1].equalsIgnoreCase("h"))
				priority = Priority.HIGH;
			else if (tokens[1].equalsIgnoreCase("urgent") || tokens[1].equalsIgnoreCase("u"))
				priority = Priority.URGENT;
				
			List<EventI> events = schedule.getEvents(priority);
			for (EventI event: events) {
				System.out.println(event);
			}
			
			System.out.println();
		} else {
			printHelp();
		}
	}
	
	private static void listEventsBasedOnTimeFrame(String userInput) {
		String[] tokens = userInput.split("\\s+");
		if (tokens.length == 3) {
			
			String[] startTimeTokens = tokens[1].trim().split("-");
			while (startTimeTokens.length != 5) {
				System.out.println("Invalid format. Enter the start time again.");
				userInput = console.nextLine();
				startTimeTokens = userInput.trim().split("-");
			}
			GregorianCalendar startTime = new GregorianCalendar(Integer.parseInt(startTimeTokens[0]),
					Integer.parseInt(startTimeTokens[1]) - 1,
					Integer.parseInt(startTimeTokens[2]), Integer.parseInt(startTimeTokens[3]),
					Integer.parseInt(startTimeTokens[4]));
			
			String[] endTimeTokens = tokens[2].trim().split("-");
			while (endTimeTokens.length != 5) {
				System.out.println("Invalid format. Enter the start time again.");
				userInput = console.nextLine();
				endTimeTokens = userInput.trim().split("-");
			}
			GregorianCalendar endTime = new GregorianCalendar(Integer.parseInt(endTimeTokens[0]),
					Integer.parseInt(endTimeTokens[1]) - 1,
					Integer.parseInt(endTimeTokens[2]), Integer.parseInt(endTimeTokens[3]),
					Integer.parseInt(endTimeTokens[4]));
			
			List<EventI> events = schedule.getEvents(startTime, endTime);
			for (EventI event: events) {
				System.out.println(event);
			}
			
			System.out.println();
		} else {
			printHelp();
		}
	}
	
	private static void checkInternet() {
		if (ServerCommunicator.checkConnection()) {
			System.out.println("Connected to the server!");
		} else {
			System.out.println("Not connected to the server!");
		}
		System.out.println();
	}
	
	// save to server (and make a local copy on the local drive so that it can be read when the client is offline)
	private static void save() {
		
		if (!ServerCommunicator.checkConnection()) {
			System.out.println("Internet is not available. You can not save the schedule!");
			System.out.println("However, you might be able to load the schedule from the local drive.");
		}
		
		if (!ServerCommunicator.isLoggedIn()) {
			System.out.println("You are not logged-in. Please log in first!");
			System.out.println();
			return;
		} 
		
		System.out.println("Note: you are about to save the content. ");
		System.out.println("Doing so will overwrite the previous copy. Do you want to continue? [Y/N]");
		String temp = console.nextLine();
		if (!(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("y"))) {
			System.out.println("You chose not to save.");
			return; 
		}
		
		
		try {
			ServerResponse serverResponse = ServerCommunicator.sendClientRequest(ServerCommunicator.generateSaveRequest(schedule));
			if (serverResponse == null) {
				System.out.println("No response from the server! You schedule is not saved.");
				System.out.println();
				return;
			}
			
			if (serverResponse.isAccepted()) {
				System.out.println("You schedule has been saved to the server.");
				saveLocally(); // it's ok to also save locally since there is no discrepancy in save file
			} else {
				System.out.println("Server rejected: "+serverResponse.getFailureNotice());
				System.out.println("You schedule is not saved.");
			}
			System.out.println();
		} catch (IOException e) {
			System.out.println("Error: cannot connect to the Internet! You schedule is not saved.");
			System.out.println();
		}
	}
	
	// the user is not supposed to not explicitly ask the program to save locally
	// it's only done when saving to server is successul (save a local copy)
	private static void saveLocally() {
		ClientIOSystem.writeToDisk(schedule, ServerCommunicator.getUsername());
		System.out.println("Schedule has been saved locally to "+ServerCommunicator.getUsername()+".SAV");
	}

	// load the schedule from the server. In some circumstances load from local drive.
	private static void load() {
		
		System.out.println("Note: you are about to load the content into the current schedule. ");
		System.out.println("Doing so will overwrite the current schedule. Do you want to continue? [Y/N]");
		String temp = console.nextLine();
		if (!(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("y"))) {
			System.out.println("You chose not to load.");
			return; 
		}
		
		// internet not available. load from local drive
		if (!ServerCommunicator.checkConnection()) {
			System.out.println("No Internet connection.");
			loadLocally();
			return;
		} 
		
		// internet is available
		if (!ServerCommunicator.isLoggedIn()) {
			System.out.println("You are not logged-in. Please log in first!");
			System.out.println();
			return;
		} 
		
		
		try {
			
			ServerResponse serverResponse = ServerCommunicator.sendClientRequest(ServerCommunicator.generateLoadRequest());
			if (serverResponse == null) {
				System.out.println("No response from the server! You are going to load from local drive.");
				loadLocally();
				System.out.println();
				return;
			}
			
			if (serverResponse.isAccepted()) {
				schedule = (Schedule)ClientIOSystem.getObject(serverResponse.getSchedule());
				System.out.println("You schedule has been loaded from the server.");
			} else {
				loadLocally();
				System.out.println("Server rejected: "+serverResponse.getFailureNotice());
				System.out.println("You schedule is loaded from local drive.");
			}
			System.out.println();
		} catch (IOException e) {
			loadLocally();
			System.out.println("Error: cannot connect to the Internet! You schedule is loaded from local drive.");
			System.out.println();
		}
	
	}
	
	private static void loadLocally() {
		System.out.println("Enter the file name you want to load from local drive.");
		String fileName = console.nextLine();
		ScheduleI tempSchedule = ClientIOSystem.loadFromDisk(fileName);
		if (tempSchedule != null){
			schedule = (Schedule) tempSchedule;
			System.out.println("The content has been loaded from local drive successfully");
			System.out.println();
		}
	}
	
	
//	private static void loadLocally(String userInput) {
//		String[] tokens = userInput.split("\\s+");
//		if (tokens.length == 2) {
//			System.out.println("Note: you are about to load the content into the current schedule. ");
//			System.out.println("Doing so will overwrite the current schedule. Do you want to continue? [Y/N]");
//			String temp = console.nextLine();
//			if (temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("y")) {
//				ScheduleI tempSchedule = ClientIOSystem.loadFromDisk(tokens[1]);
//				if (tempSchedule != null){
//					schedule = (Schedule) tempSchedule;
//					System.out.println("The content has been loaded successfully");
//				}
//			} else {
//				System.out.println("You chose not to load.");
//			}
//			System.out.println();
//		} else {
//			printHelp();
//		}
//	}
	
	private static void create(String userInput) {
		
		String[] tokens = userInput.split("\\s+");
		if (tokens.length == 3) {
			if (ServerCommunicator.isLoggedIn()) {
				System.out.println("You have already logged in. Cannot create!"); // TODO you should be able to CREATE
				return;
			}
			
			try {
				// TODO encryption
				ServerResponse serverResponse = ServerCommunicator.sendClientRequest(ServerCommunicator.generateCreateRequest(tokens[1], tokens[2]));
				if (serverResponse == null) {
					System.out.println("No response from the server!");
					System.out.println();
					return;
				}
				if (serverResponse.isAccepted()) {
					System.out.println("Registration done successfully! Please save your username and password.");
					System.out.println("You are now logged in");
				} else {
					System.out.println("Server rejected: "+serverResponse.getFailureNotice());
				}
				System.out.println();
			} catch (IOException e) {
				System.out.println("Error: cannot connect to the Internet!");
				System.out.println();
			}
		} else {
			printHelp();
		}
		
	}
	
	private static void login(String userInput) {
		
		String[] tokens = userInput.split("\\s+");
		if (tokens.length == 3) {
			if (ServerCommunicator.isLoggedIn()) {
				System.out.println("You have already logged in. Cannot login!"); // TODO you should be able to LOGIN...
				return;
			}
			
			try {
				// TODO encryption
				ServerResponse serverResponse = ServerCommunicator.sendClientRequest(ServerCommunicator.generateLoginRequest(tokens[1], tokens[2]));
				if (serverResponse == null) {
					System.out.println("No response from the server!");
					System.out.println();
					return;
				}
				
				if (serverResponse.isAccepted()) {
					System.out.println("You are now logged in. Do you want to load the schedule? [Y/N]");
					String temp = console.nextLine();
					if (!(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("y"))) {
						System.out.println("You chose not to load.");
					} else {
						load();
					}
				} else {
					System.out.println("Server rejected: "+serverResponse.getFailureNotice());
				}
				System.out.println();
			} catch (IOException e) {
				System.out.println("Error: cannot connect to the Internet!");
				System.out.println();
			}
		} else {
			printHelp();
		}
		
	}
	
	private static void requestAuthenticate(String userInput) {
		
		String[] tokens = userInput.split("\\s+");
		if (tokens.length == 2) {
			if (!ServerCommunicator.isLoggedIn()) {
				System.out.println("You are not logged-in. Please log in first!");
				System.out.println();
				return;
			} 
			ServerCommunicator.setAuthenticated(false);
			
			
			try {
				ServerResponse serverResponse = ServerCommunicator.sendClientRequest(ServerCommunicator.generateRequestAuthRequest(tokens[1]));
				if (serverResponse == null) {
					System.out.println("No response from the server!");
					System.out.println();
					return;
				}
				
				if (serverResponse.isAccepted()) {
					ServerCommunicator.setPhoneNumber(tokens[1]);
					System.out.println("Server has received your authentication request. Please wait for a while.");
					System.out.println("An authentication text message will arrive in approximately 3 minutes.");
					System.out.println("If you don't get it, please request the authentication code one more time.");
				} else {
					System.out.println("Server rejected: "+serverResponse.getFailureNotice());
				}
				System.out.println();
			} catch (IOException e) {
				System.out.println("Error: cannot connect to the Internet!");
				System.out.println();
			}
		} else {
			printHelp();
		}
		
	}
	
	private static void authenticate(String userInput) {
		String[] tokens = userInput.split("\\s+");
		if (tokens.length == 2) {
			if (!ServerCommunicator.isLoggedIn()) {
				System.out.println("You are not logged-in. Please log in first!"); 
				System.out.println();
				return;
			} else if (ServerCommunicator.isAuthenticated()) { // TODO serverCommunicator should not keep the authenticated field?? 
																// should do the authentication check on the server side?
				if (ServerCommunicator.getPhoneNumber() ==  null) { 
					System.out.println("You have not requested authentication in this session yet. Please request authentication first.");
				} else {
					System.out.println("You current phone number "+ ServerCommunicator.getPhoneNumber()+ " has already been authenticated.");
					System.out.println("If you wish to change the phone number associated with your account, request authentication first.");
				}
				System.out.println();
				return;
			} 
			
			try {
				ServerResponse serverResponse = ServerCommunicator.sendClientRequest(ServerCommunicator.generateAuthenticateRequest(tokens[1]));
				if (serverResponse == null) {
					System.out.println("No response from the server!");
					System.out.println();
					return;
				}
				
				if (serverResponse.isAccepted()) {
					ServerCommunicator.setAuthenticated(true);
					System.out.println("Your phone number has been authenticated. You can get alerts from the server from now on.");
				} else {
					System.out.println("Server rejected: "+serverResponse.getFailureNotice());
				}
				System.out.println();
			} catch (IOException e) {
				System.out.println("Error: cannot connect to the Internet!");
			}
		} else {
			printHelp();
		}
	}

	private static void requestAlert(String userInput) { // TODO handle names that include space
		String[] tokens = userInput.split("\\s+");
		if (tokens.length >= 3) {
			if (!ServerCommunicator.isLoggedIn()) {
				System.out.println("You are not logged-in. Please log in first!"); 
				System.out.println();
				return;
			} 

			// check if the category exists or not
			CategoryI ctgr = schedule.getCategoriesMap().get(tokens[1]);
			if (ctgr == null) {
				System.out.println("The category does not exist!");
				return;
			}
			
			// check if the event exists or not			
			EventI event = null;
			for (int i = 0; i < ctgr.getAllEvents().size(); i++) {
				if (ctgr.getAllEvents().get(i).getName().equals(tokens[2])) {
					event = ctgr.getAllEvents().get(i);
					break;
				}
			}
			if (event == null) {
				System.out.println("The event does not exist!");
				return;
			}
			
			if (!event.hasAlert()) {
				System.out.println("The event does not have an alert yet! Edit it first");
				return;
			}
			
			try {
				ClientRequest alertRequest = ServerCommunicator.generateAlertRequest
						(ServerCommunicator.getUsername()+"."+ctgr.getName()+"."+event.getName(), event.getAlertText(), event.getRepeating().toString(), event.getAlertTime());
				ServerResponse serverResponse = ServerCommunicator.sendClientRequest(alertRequest);
				if (serverResponse == null) {
					System.out.println("No response from the server!");
					System.out.println();
					return;
				}
				
				if (serverResponse.isAccepted()) {
					System.out.println("Server has processed your request. You will get alerts for the "+tokens[2]+" event in "+tokens[1]+" category.");
				} else {
					System.out.println("Server rejected: "+serverResponse.getFailureNotice());
				}
				System.out.println();
			} catch (IOException e) {
				System.out.println("Error: cannot connect to the Internet!");
				System.out.println();
			}
			
		} else {
			printHelp();
		}	
	}
	
	private static void exit() {
		System.out.println("Bye");
		System.exit(0);
	}

	private static void printHelp(){
		System.out.println("Commands:");
		System.out.println("add_ctgr <category_name> - adds a category to the schedule.");
		System.out.println("rm_ctgr <category_name> - removes a category from the schedule.");
		System.out.println("rename_ctgr <original_name> <new_name> - change the name of a category.");
		System.out.println("list_ctgrs - lists all the categories in the schedule.");
		System.out.println("create_event - creates an event and add it to the corresponding category.");
		System.out.println("edit_event <category_name> <event_name> - modifies a field in an event.");
		System.out.println("list_events_using_ctgr <category_name> - lists all the events in an category.");
		System.out.println("list_events_using_priority <priority> - lists all the events of a specified priority[L/M/H/U].");
		System.out.println("list_events_using_times <start_time> <end_time> - lists all the events within the specified time frame.");
		System.out.println("check_inet - see if the client is connected to the Internet.");
		System.out.println("save - saves the content of the current schedule to the server side.");
		System.out.println("load - first trys loading from the server side. If there is no connection, load from local drive.");
		System.out.println("create <username> <password> - creates a new account.");
		System.out.println("login <username> <password> - logs in using you account.");
		System.out.println("request_auth <phone_number> - requests an authentication code, which will be sent to you phone.");
		System.out.println("auth <authentication_code> - enters the authentication code; this command should be entered only after the user has requested authentication.");
		System.out.println("request_alert <category_name> <event_name> - lets the server send an alert for an event. (if you edit the alert of an event, you also need to use this command).");
		System.out.println("exit or quit - quit the program");
		System.out.println("help - see a list of commands");
		System.out.println();
	}

}
