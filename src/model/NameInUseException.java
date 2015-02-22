package model;

public class NameInUseException extends Exception {
	
	private String name;
	
	public NameInUseException(String name) {
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
}
