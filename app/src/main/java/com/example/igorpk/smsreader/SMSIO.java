package com.example.igorpk.smsreader;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by igorpk on 10/17/15.
 */
public class SMSIO {

    /**
     * @Info
        Column ID    -   Column Name
        0  :  _id
        1  : thread_id
        2  : address
        3  : person
        4  : date
        5  : protocol
        6  : read
        7  : status
        8  : type
        9  : reply_path_present
        10 : subject
        11 : body
        12 : service_center
        13 : locked
     */
    // holds the whitelisted origin telephone numbers.
    private ArrayList originNumbers;

    // timestamp of last record in DB, used as filter.
    private Long lastTimestamp;

    private Context context;

    public SMSIO(Context context){
        this.context=context;
    }

//    public SMSIO(ArrayList originNumbers, Long last_timestamp) {
//        this.originNumbers = originNumbers;
//        this.lastTimestamp = last_timestamp;
//    }

    /**
     * Apply filters to smses and return collection
     */
    public ArrayList run(Long lastTimestamp) {
        ArrayList results = new ArrayList<>();

        // Define the source uri
        Uri inboxURI = Uri.parse("content://sms/inbox");

        // Query columns
        String[] dbColumns = new String[]{"_id", "address", "body", "date"};

        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = context.getContentResolver();

        // Fetch Inbox SMS Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, dbColumns, null, null, null);

        // Iterate over cursor to populate a string of messages
        while (c.moveToNext()) {

            Long smsDate = c.getLong(3);

            if (smsDate > lastTimestamp) {
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
