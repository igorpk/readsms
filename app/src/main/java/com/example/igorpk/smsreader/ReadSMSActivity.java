package com.example.igorpk.smsreader;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Read SMS Messages on the Android device
 *

 @TODO: Consider filtering messages based on MSISDN. Check Number -> If in FNB whitelist (+27820070***), continue -> Else skip.
 */
public class ReadSMSActivity extends Activity {

    TextView textOutput;

    // Array of retrieved messages
    ArrayList<String> smsAggregate;
    String output;

    static final String DTAG = "ReadSMSActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readsms);

        DBHelper db = new DBHelper(this);

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

        // Get messages
        smsAggregate = new SMSIO(this).run(oneMonthAgo);

        // Parse Messages
        Lexer lexer = new Lexer(smsAggregate);
        ArrayList<String> parsedMessages = lexer.getSmslist();

        Double output = 0.00;

        for(String amount : parsedMessages) {
            output += Double.parseDouble(amount);
        }

        /* @TODO POST a json payload to a host too? this.jason(smsAggregate.toArray()) */
        textOutput.setText(output.toString());
    }
}