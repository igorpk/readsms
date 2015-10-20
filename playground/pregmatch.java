import java.lang.Exception;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.HashMap;

public class pregmatch {

    final static String in[] = {"paid from","withdrawn from","reserved for purchase"};
    final static String out[] = {"paid to"};


	public static void main(String[] args) {

		//final String smstext = "FNB :-) R404.00 paid from cheq a/c..529405 @ Online Banking. Avail R404.00. Ref.Referencia. 2Oct 14:58";
		final String smstext = "FNB :-) R404.00 paid to cheq a/c..529405 @ EFT. Ref.Referencia. 2Oct 14:58";
        final String direction;

        HashMap<String, String> parsedSMS = new HashMap<String, String>();

        Pattern contentPattern = Pattern.compile("(?<name>FNB \\:\\-\\)) (?<value>R[0-9]*\\.[0-9]{2}) (?<action>paid from|paid to|withdrawn from|reserved for purchase)\\s+");
        Matcher contentMatcher = contentPattern.matcher(smstext);

        try {
             if (contentMatcher.find()) {
                 parsedSMS.put("Name", contentMatcher.group("name"));
                 parsedSMS.put("Value", contentMatcher.group("value"));
                 parsedSMS.put("Action", contentMatcher.group("action"));
             }
             else {
                 throw new Exception("No matching groups found");
             }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        // Loop over the check arrays
        for(String value : in) {
            parsedSMS.put("Direction", "In");
        }

        for(String value : out) {
            parsedSMS.put("Direction", "Out");
        }



        // Iterate and print out the contents of the HashMap
        Iterator<String> mapIterator = parsedSMS.keySet().iterator();
        while(mapIterator.hasNext()) {
            String key = mapIterator.next();
            System.out.println(key + ": " + parsedSMS.get(key) );
        }

	}

}
