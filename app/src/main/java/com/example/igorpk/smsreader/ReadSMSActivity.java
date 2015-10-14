package com.example.igorpk.smsreader;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadSMSActivity extends Activity {

    //  GUI Widget
    TextView lvMsg;

    String smsAggregate;

    // Cursor Adapter
    SimpleCursorAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readsms);

        lvMsg = (TextView) findViewById(R.id.lvMsg);

        // Define the source uri
        Uri inboxURI = Uri.parse("content://sms/inbox");

        // List required columns
//        Column ID    -   Column Name
//
//        0                      :      _id
//        1                      :     thread_id
//        2                      :     address
//        3                      :     person
//        4                      :     date
//        5                      :     protocol
//        6                      :     read
//        7                      :    status
//        8                      :    type
//        9                      :    reply_path_present
//        10                   :    subject
//        11                   :    body
//        12                   :    service_center
//        13                   :    locked
        String[] dbColumns = new String[] { "_id", "address", "body", "date" };

        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = getContentResolver();

        // Fetch Inbox SMS Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, dbColumns, null, null, null);

        // Set current timestamp for comparison later
        long back24Hours = (System.currentTimeMillis() - 84600000);

        // Iterate over cursor to populate a string of messages
        while(c.moveToNext()) {

            long smsDate = Long.parseLong(c.getString(3));

            if(smsDate > (back24Hours) ) {
                // We only want to retrieve SMS messages from FNB at this point
                Pattern mPattern = Pattern.compile("^FNB\\s\\W{3}\\sR?.(\\d*\\.\\d*)");

                Matcher matcher = mPattern.matcher(c.getString(2));
                if(matcher.find())
                {
                    smsAggregate += matcher.group(0) +  "\n";
                    //smsAggregate += matcher.toString() +  "\n";
                    //smsAggregate += smsDate + " " + back24Hours +  "\n";
                }
            }
        }

        lvMsg.setText(smsAggregate);
//        Attached Cursor with adapter and display in listview
//        adapter = new SimpleCursorAdapter(this, R.layout.row, c,
//                new String[] { "body", "address" }, new int[] {
//                R.id.lblMsg, R.id.lblNumber });
//        lvMsg.setAdapter(adapter);
    }
}