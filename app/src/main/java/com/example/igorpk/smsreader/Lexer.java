package com.example.igorpk.smsreader;

import android.util.Log;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by igorpk on 10/17/15.
 *
 * Lexer
 */
public class Lexer {

    private ArrayList<String> smslist = new ArrayList<>();

    public Lexer(ArrayList<String> smstext) {

        FNBLexerProvider provider = new FNBLexerProvider();

        for(String sms : smstext) {
            this.parseSMS(sms, provider);
        }
    }

    public ArrayList<String> getSmslist() {
        return smslist;
    }

    private void parseSMS(String sms, FNBLexerProvider provider) {
        Pattern contentPattern = Pattern.compile(provider.getPattern());
        Matcher contentMatcher = contentPattern.matcher(sms);

        if (contentMatcher.find()) {
            String sign = "";
            String value = contentMatcher.group(2);

            for (String outString : provider.getOut()) {
                Integer comparisonResult = outString.compareTo(contentMatcher.group(3));
                Log.d("Match Attempt::", outString + ":" + contentMatcher.group(3));
                if (comparisonResult == 0) {
                    sign = "-";
                    break;
                }
            }
            Log.d("Match Hit!!", sign+value);
            String output = sign+value;
            this.smslist.add(output);
        }
    }
}
