package console;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import utilities.IOSystem;
import model.CategoryI;
import model.Event;
import model.EventI;
import model.NameInUseException;
import model.Schedule;
import model.EventI.Repeat;
import model.EventI.Priority;
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
			addCategory(userInput);
		} else if (userInput.startsWith("rm_ctgr")){
			removeCategory(userInput);
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
		} else if (userInput.startsWith("save")){
			save(userInput);
		} else if (userInput.startsWith("load")){
			load(userInput);
		} else if (userInput.equals("exit") || userInput.equals("quit")) { 
			exit();
		} else {
			printHelp();
		} 
	} 
	
	public static void addCategory(String userInput) {
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
	
	public static void removeCategory(String userInput) {
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
	
	public static void listCategories() {
		for (CategoryI category: schedule.getCategories()) {
			System.out.println(category);
		}
		System.out.println();
	}
	
	public static void createEvent() {
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
	
	public static void editEvent(String userInput) {
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
	
	public static void listEventsBasedOnCategory(String userInput) {
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
	
	public static void listEventsBasedOnPriority(String userInput) {
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
	
	public static void listEventsBasedOnTimeFrame(String userInput) {
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
	
	public static void save(String userInput) {
		String[] tokens = userInput.split("\\s+");
		if (tokens.length == 2) {
			IOSystem.writeToDisk(schedule, tokens[1]);
			System.out.println("Schedule has been saved successfully.");
			System.out.println();
		} else {
			printHelp();
		}
	}
	
	public static void load(String userInput) {
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
	}
	
	public static void exit() {
		System.out.println("Bye");
		System.exit(0);
	}

	private static void printHelp(){
		System.out.println("Commands:");
		System.out.println("add_ctgr <category_name> - adds a category to the schedule.");
		System.out.println("rm_ctgr <category_name> - removes a category from the schedule.");
		System.out.println("list_ctgrs - lists all the categories in the schedule.");
		System.out.println("create_event - creates an event and add it to the corresponding category.");
		System.out.println("edit_event - modifies a field in an event.");
		System.out.println("list_events_using_ctgr <category_name> - lists all the events in an category.");
		System.out.println("list_events_using_priority <priority> - lists all the events of a specified priority[L/M/H/U].");
		System.out.println("list_events_using_times <start_time> <end_time> - lists all the events within the specified time frame.");
		System.out.println("save <file_name> - save the content of the current schedule into <file_name>.SAV");
		System.out.println("load <file_name> - load from <file_name>.SAV");
		System.out.println("exit or quit - quit the program");
		System.out.println("help - see a list of commands");
		System.out.println();
	}

}
