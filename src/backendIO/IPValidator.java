package backendIO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class IPValidator{

	private static final String IPADDRESS_PATTERN = 
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	private static Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
	private static Matcher ipMatcher;

	public static boolean validate(String ip){		  
		ipMatcher = pattern.matcher(ip);
		return ipMatcher.matches();	    	    
	}
}