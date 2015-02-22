package model;

import java.io.Serializable;
import java.io.Writer;
import java.io.Reader;
import java.util.List;

public interface ObligationI extends Serializable {

	public void writeTo(Writer writer);
	
	public void readFrom(Reader reader);
	
	public boolean addEvent(EventI event);
	
	public boolean checkEvent(EventI event);
	
	public List<EventI> getCore(ObligationI obligation);
	
	public List<EventI> getChildren(ObligationI obligation);

	public List<EventI> get();
}

