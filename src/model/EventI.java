package model;

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

public interface EventI extends Serializable {

	public void writeTo(Writer writer);
	
	public void readFrom(Reader reader);
	
}
