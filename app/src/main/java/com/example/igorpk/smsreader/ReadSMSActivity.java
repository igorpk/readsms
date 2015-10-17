package com.example.igorpk.smsreader;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Read SMS Messages on the Android device
 *

 @TODO: Consider filtering messages based on MSISDN. Check Number -> If in FNB whitelist (+27820070***), continue -> Else skip.
 */
public class ReadSMSActivity extends Activity {

    TextView textOutput;

    // Array of computed values
    ArrayList<String> smsAggregate;

    static final String DTAG = "ReadSMSActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readsms);

        UploadQueueDbHelper db = new UploadQueueDbHelper(this);

        textOutput = (TextView) findViewById(R.id.lvMsg);

        // Get our date on
        Calendar calendar = Calendar.getInstance();

        // Set calendar to the beginning of the month
        // This can (and should) move out of this class and become configurable via parameters
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long oneMonthAgo = calendar.getTimeInMillis();

        smsAggregate = new SMSIO(this).run(oneMonthAgo);

        /* @TODO POST a json payload to a host too? this.jason(smsAggregate.toArray()) */
        textOutput.setText(String.valueOf(smsAggregate) + "w00t!!");
    }

    /**
     * Parse the SMS Inbox of the device in order to apply a regex to each.
     *
     * @param startDate Long
     * @return ArrayList
     */
    public ArrayList parseInbox(Long startDate) {

        ArrayList results = new ArrayList<>();

        // Define the source uri
        Uri inboxURI = Uri.parse("content://sms/inbox");

        // Query columns
        String[] dbColumns = new String[]{"_id", "address", "body", "date"};

        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = getContentResolver();

        // Fetch Inbox SMS Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, dbColumns, null, null, null);

        // Iterate over cursor to populate a string of messages
        while (c.moveToNext()) {

            Long smsDate = c.getLong(3);

            if (smsDate > startDate) {
                /**
                 * @TODO
                 * BankLexer($bank)
                 * construct($bank) { switch $bank, return regex }
                 */
                // Simply checks for a monetary value
                Pattern contentPattern = Pattern.compile("\\w\\d*\\.\\d{2}");
                Matcher contentMatcher = contentPattern.matcher(c.getString(2));

                if (contentMatcher.find()) {
                    String regexMatch = contentMatcher.group(0);
                    results.add(regexMatch);
                }
            }
        }
        return results;
    }
}