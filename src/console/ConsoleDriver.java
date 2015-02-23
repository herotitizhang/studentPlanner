package console;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Scanner;

import model.Category;
import model.Event;
import model.NameInUseException;
import model.Schedule;
import model.Event.Repeat;

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
				} catch (NameInUseException e) {
					System.out.println("The name "+ e.getName()+" is used!");
					e.printStackTrace();
				}
			} else {
				printHelp();
			}
		} else if (userInput.startsWith("rm_ctgr")){
			String[] tokens = userInput.split("\\s+");
			if (tokens.length == 2) {
				schedule.removeCategory(tokens[1]);
			} else {
				printHelp();
			}
		} else if (userInput.equals("list_ctgrs")) {
			for (Category category: schedule.getCategories()) {
				System.out.println(category);
			}
		} else if (userInput.equals("create_event")){
			// get name
			System.out.println("Please enter the event name.");
			String name = console.nextLine();
			
			// get text
			System.out.println("Please enter the description of the event.");
			String text = console.nextLine();
			
			// get startTime
			System.out.println("Please enter the start time in such a format yyyy-mm-dd-hh-mm.");
			String temp = console.nextLine();
			String[] tokens = temp.split("-");
			while (tokens.length != 5) {
				System.out.println("Invalid format. Enter it again.");
				temp = console.nextLine();
				tokens = temp.split("-");
			}
			GregorianCalendar startTime = new GregorianCalendar(Integer.parseInt(tokens[0]),
					Integer.parseInt(tokens[1])-1, Integer.parseInt(tokens[2]), 
					Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
			
			// get endTime
			System.out.println("Please enter the end time in such a format yyyy-mm-dd-hh-mm.");
			temp = console.nextLine();
			tokens = temp.split("-");
			while (tokens.length != 5) {
				System.out.println("Invalid format. Enter it again.");
				temp = console.nextLine();
				tokens = temp.split("-");
			}
			GregorianCalendar endTime = new GregorianCalendar(Integer.parseInt(tokens[0]),
					Integer.parseInt(tokens[1])-1, Integer.parseInt(tokens[2]), 
					Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
			
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
				tokens = temp.split("-");
				while (tokens.length != 5) {
					System.out.println("Invalid format. Enter it again.");
					temp = console.nextLine();
					tokens = temp.split("-");
				}
				alertTime = new GregorianCalendar(Integer.parseInt(tokens[0]),
						Integer.parseInt(tokens[1])-1, Integer.parseInt(tokens[2]), 
						Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
			}
			
			// get repeat NONE, WEEKLY, MONTHLY, YEARLY;
			System.out.println("Do you want it to repeat weekly, monthly, yearly or does not repeat? [W/M/Y/N]");
			Event.Repeat repeat = Event.Repeat.NONE;
			temp = console.nextLine();
			if (temp.equalsIgnoreCase("weekly") || temp.equalsIgnoreCase("w"))
				repeat = Event.Repeat.WEEKLY;
			else if (temp.equalsIgnoreCase("monthly") || temp.equalsIgnoreCase("m"))
				repeat = Event.Repeat.MONTHLY;
			else if (temp.equalsIgnoreCase("yearly") || temp.equalsIgnoreCase("y"))
				repeat = Event.Repeat.YEARLY;

			// get category
			System.out.println("What category is it?");
			Category category = null;
			temp = console.nextLine();
			if (schedule.getCategoriesMap().containsKey(temp)){
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
					alertTime, repeat, category); // the constructor will add the event to the category
		} else if (userInput.startsWith("list_events")) {
			String[] tokens = userInput.split("\\s+");
			if (tokens.length == 2) {
				if (schedule.getCategoriesMap().containsKey(tokens[1])) {
					Category category = schedule.getCategoriesMap().get(tokens[1]);
					for (Event event: category.getAllEvents()) {
						System.out.println(event);
					}
				}
			} else {
				printHelp();
			}
			
		} else if (userInput.startsWith("exit")) { 
			System.out.println("Bye");
			System.exit(0);
		} else {
			printHelp();
		} 
	}

	private static void printHelp(){
		System.out.println("Commands:");
		System.out.println("add_ctgr <category_name> - adds a category to the schedule.");
		System.out.println("rm_ctgr <category_name> - removes a category from the schedule.");
		System.out.println("list_ctgrs - lists all the categories in the schedule.");
		System.out.println("create_event - creates an event and add it to the corresponding category.");
		System.out.println("list_events <category_name> - lists all the events in an category.");
		System.out.println("exit - quit the program");
		System.out.println("help - see a list of commands");
	}

}
