package console;

import ioUtilities.IOSystem;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Scanner;

import model.Category;
import model.CategoryI;
import model.Event;
import model.EventI;
import model.NameInUseException;
import model.Schedule;
import model.Event.Repeat;
import model.ScheduleI;

public class ConsoleDriver {

	public static Schedule schedule = new Schedule();
	public static Scanner console = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("Enter \"help\" to see a list of available commands");
		while (console.hasNextLine()){
			String userInput = console.nextLine();
			processUserInput(userInput);
		}
	}
	
	public static void processUserInput(String userInput) {
		if (userInput.startsWith("add_ctgr")){
			String[] tokens = userInput.split("\\s+");
			if (tokens.length == 2) {
				try {
					schedule.addCategory(tokens[1]);
					System.out.println("The category named \""+tokens[1]+"\" has been added successfully!");
				} catch (NameInUseException e) {
					System.out.println("The name "+ e.getName()+" has been used!");
//					e.printStackTrace();
				} finally {
					System.out.println();
				}
			} else {
				printHelp();
			}
		} else if (userInput.startsWith("rm_ctgr")){
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
		} else if (userInput.equals("list_ctgrs")) {
			for (CategoryI category: schedule.getCategories()) {
				System.out.println(category);
			}
			System.out.println();
		} else if (userInput.equals("create_event")){ 
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
				System.out.println("Please enter the start time in such a format yyyy-mm-dd-hh-mm.");
				temp = console.nextLine();
				tokens =  temp.trim().split("-");
				while (tokens.length != 5) { //TODO
					System.out.println("Invalid format. Enter it again.");
					temp = console.nextLine();
					tokens = temp.trim().split("-");
				}
				startTime = new GregorianCalendar(Integer.parseInt(tokens[0]),
						Integer.parseInt(tokens[1])-1, Integer.parseInt(tokens[2]), 
						Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));

				// get endTime
				System.out.println("Please enter the end time in such a format yyyy-mm-dd-hh-mm.");
				temp = console.nextLine();
				tokens = temp.trim().split("-");
				while (tokens.length != 5) { //TODO
					System.out.println("Invalid format. Enter it again.");
					temp = console.nextLine();
					tokens = temp.trim().split("-");
				}
				endTime = new GregorianCalendar(Integer.parseInt(tokens[0]),
						Integer.parseInt(tokens[1])-1, Integer.parseInt(tokens[2]), 
						Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));

				if (startTime.compareTo(endTime) <= 0) { // qualified startTime and endTime
					break;
				} else {
					System.out.println("Invalid times: end time is before starting time!");
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
				System.out.println("Please enter the alert time in such a format yyyy-mm-dd-hh-mm.");
				temp = console.nextLine();
				tokens = temp.trim().split("-");
				while (tokens.length != 5) {
					System.out.println("Invalid format. Enter it again.");
					temp = console.nextLine();
					tokens = temp.trim().split("-");
				}
				alertTime = new GregorianCalendar(Integer.parseInt(tokens[0]),
						Integer.parseInt(tokens[1])-1, Integer.parseInt(tokens[2]), 
						Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
			}
			
			// get repeat NONE, WEEKLY, MONTHLY, YEARLY;
			System.out.println("Do you want it to repeat weekly, monthly, yearly or does not repeat? [W/M/Y/N]");
			EventI.Repeat repeat = EventI.Repeat.NONE;
			temp = console.nextLine();
			if (temp.equalsIgnoreCase("weekly") || temp.equalsIgnoreCase("w"))
				repeat = EventI.Repeat.WEEKLY;
			else if (temp.equalsIgnoreCase("monthly") || temp.equalsIgnoreCase("m"))
				repeat = EventI.Repeat.MONTHLY;
			else if (temp.equalsIgnoreCase("yearly") || temp.equalsIgnoreCase("y"))
				repeat = EventI.Repeat.YEARLY;
			
			// get priority LOW, MEDIUM, HIGH, URGENT;
			System.out.println("What is the priority of the event (low, medium, high or urgent) ? [L/M/H/U]");
			EventI.Priority priority = EventI.Priority.MEDIUM;
			temp = console.nextLine();
			if (temp.equalsIgnoreCase("low") || temp.equalsIgnoreCase("l"))
				priority = EventI.Priority.LOW;
			else if (temp.equalsIgnoreCase("high") || temp.equalsIgnoreCase("h"))
				priority = EventI.Priority.HIGH;
			else if (temp.equalsIgnoreCase("urgent") || temp.equalsIgnoreCase("u"))
				priority = EventI.Priority.URGENT;
			
			// get category
			System.out.println("What category is it? Below are existing categories:");
			for (CategoryI category: schedule.getCategories()) 
				System.out.print(category+" ");
			CategoryI category = null;
			temp = console.nextLine();
			if (temp.trim().length() == 0) {
				System.out.println("You didn't enter the category, so the event belongs to the default category.");
				category = schedule.getCategoriesMap().get("default");
			} else if (schedule.getCategoriesMap().containsKey(temp)){
				category = schedule.getCategoriesMap().get(temp);
			} else {
				try {
					category = schedule.addCategory(temp);
				} catch (NameInUseException e) {
					System.out.println("The name "+ e.getName()+" is used!");
					e.printStackTrace();
				}
			}
			
			new Event(name, text, startTime, endTime, alert, alertText,
					alertTime, repeat, priority, category); // the constructor will add the event to the category
			System.out.println("An event named \""+name+"\" has been created!");
			System.out.println();
		} else if (userInput.startsWith("edit_event")) { 

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
							System.out.println("name, text, start time, end time, alert, alert text, alert time, repeat and category");

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

							} else if (command.equals("alert text")) {
								if (event.hasAlert()) {
									System.out.println("Please enter the new alert text:");
									event.setAlertText(console.nextLine());
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
								} else {
									System.out.println("Please turn on the alert before you want to set the alert time.");
								}
							} else if (command.equals("repeat")) {
								System.out.println("Do you want it to repeat weekly, monthly, yearly or does not repeat? [W/M/Y/N]");
								event.setRepeating(EventI.Repeat.NONE);
								String temp = console.nextLine();
								if (temp.equalsIgnoreCase("weekly") || temp.equalsIgnoreCase("w"))
									event.setRepeating(EventI.Repeat.WEEKLY);
								else if (temp.equalsIgnoreCase("monthly") || temp.equalsIgnoreCase("m"))
									event.setRepeating(EventI.Repeat.MONTHLY);
								else if (temp.equalsIgnoreCase("yearly") || temp.equalsIgnoreCase("y"))
									event.setRepeating(EventI.Repeat.YEARLY);

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

		} else if (userInput.startsWith("list_events")) {
			String[] tokens = userInput.split("\\s+");
			if (tokens.length == 2) {
				if (schedule.getCategoriesMap().containsKey(tokens[1])) {
					CategoryI category = schedule.getCategoriesMap().get(tokens[1]);
					for (EventI event: category.getAllEvents()) {
						System.out.println(event);
					}
				}
				System.out.println();
			} else {
				printHelp();
			}
			
		} else if (userInput.equals("exit") || userInput.equals("quit")) { 
			System.out.println("Bye");
			System.exit(0);
		} else if (userInput.startsWith("save")){
			String[] tokens = userInput.split("\\s+");
			if (tokens.length == 2) {
				IOSystem.writeToDisk(schedule, tokens[1]);
				System.out.println("Schedule has been saved successfully.");
				System.out.println();
			} else {
				printHelp();
			}
		} else if (userInput.startsWith("load")){
			String[] tokens = userInput.split("\\s+");
			if (tokens.length == 2) {
				
				System.out.println("Note: you are about to load the content into the current schedule. ");
				System.out.println("Doing so will overwrite the current schedule. Do you want to continue? [Y/N]");
				String temp = console.nextLine();
				if (temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("y")) {
					ScheduleI tempSchedule = IOSystem.loadFromDisk(tokens[1]);
					if (tempSchedule != null){
						schedule = (Schedule) tempSchedule;
						System.out.println("The content has been loaded successfully");
					}
				} else {
					System.out.println("You chose not to load.");
				}
				System.out.println();

			} else {
				printHelp();
			}
		} else {
			printHelp();
		} 
	} // TODO put if-statements in separate methods 

	private static void printHelp(){
		System.out.println("Commands:");
		System.out.println("add_ctgr <category_name> - adds a category to the schedule.");
		System.out.println("rm_ctgr <category_name> - removes a category from the schedule.");
		System.out.println("list_ctgrs - lists all the categories in the schedule.");
		System.out.println("create_event - creates an event and add it to the corresponding category.");
		System.out.println("edit_event - modifies a field in an event.");
		System.out.println("list_events <category_name> - lists all the events in an category.");
		System.out.println("save <file_name> - save the content of the current schedule into <file_name>.SAV");
		System.out.println("load <file_name> - load from <file_name>.SAV");
		System.out.println("exit or quit - quit the program");
		System.out.println("help - see a list of commands");
		System.out.println();
	}

}
