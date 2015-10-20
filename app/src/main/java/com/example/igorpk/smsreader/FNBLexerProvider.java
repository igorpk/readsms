package com.example.igorpk.smsreader;

/**
 * Created by igorpk on 10/17/15.
 */
public class FNBLexerProvider {

    private final String in[] = {"paid to"};
    private final String out[] = {"paid from","withdrawn from","reserved","t/fer to"};
    private final String pattern = "(FNB \\:\\-\\)) R([0-9]*\\.[0-9]{2}) (paid from|paid to|withdrawn from|reserved|t/fer to)\\s+";

    public String[] getIn() {
        return in;
    }

    public String[] getOut() {
        return out;
    }

    public String getPattern() {
        return pattern;
    }
}
