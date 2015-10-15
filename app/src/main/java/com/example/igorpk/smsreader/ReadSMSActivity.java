package com.example.igorpk.smsreader;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Read SMS Messages on the Android device
 *
 *         Column ID    -   Column Name

 0                      :      _id
 1                      :     thread_id
 2                      :     address
 3                      :     person
 4                      :     date
 5                      :     protocol
 6                      :     read
 7                      :    status
 8                      :    type
 9                      :    reply_path_present
 10                   :    subject
 11                   :    body
 12                   :    service_center
 13                   :    locked

 @TODO: Consider filtering messages based on MSISDN. Check Number -> If in FNB whitelist (+27820070***), continue -> Else skip.
 */
public class ReadSMSActivity extends Activity {

    TextView textOutput;

    /* @TODO: Figure out how to change this to a float. */
    ArrayList<String> smsAggregate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readsms);

        textOutput = (TextView) findViewById(R.id.lvMsg);

        smsAggregate = new ArrayList<String>();

        // Define the source uri
        Uri inboxURI = Uri.parse("content://sms/inbox");

        // Query columns
        /* @TODO: Review the fields needed - maybe there is an easier way? */
        String[] dbColumns = new String[] { "_id", "address", "body", "date" };

        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = getContentResolver();

        // Fetch Inbox SMS Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, dbColumns, null, null, null);

        /* @TODO: Convert this to 1 month - function to convert seconds. */
        // Set current timestamp for comparison later
        long back24Hours = (System.currentTimeMillis() - 84600000);

        // Iterate over cursor to populate a string of messages
        while(c.moveToNext()) {
            /* @TODO: GET RID OF THE FUCKING null IN THE RESULTS!. */

            long smsDate = Long.parseLong(c.getString(3));
            float total = 0;
            if (smsDate > (back24Hours)) {
                // We only want to retrieve SMS messages from FNB at this point
                /* @TODO:   Expand on this regex - it works, but it's too broad. The goal is to
                   @TODO:   isolate the monetary portion of the sms only when from FNB */
                // \\d*     any amount of digits
                // \\.      followed by one period
                // \\d{2}   followed by two digits
                Pattern contentPattern = Pattern.compile("\\d*\\.\\d{2}");

                Matcher contentMatcher = contentPattern.matcher(c.getString(2));

                /* @TODO: change find() to match()?  */
                if (contentMatcher.find()) {
                        String mew = contentMatcher.group(0);
                        smsAggregate.add(mew);
                }
            }
        }
        /* @TODO POST a json payload to a host too?*/
        textOutput.setText(String.valueOf(smsAggregate));
    }
}