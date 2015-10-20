package com.example.igorpk.smsreader;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by roland on 10/10/15.
 * Mangled by igor 17/10/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 2;  //used by the parent class to determine whether to run
                                            //onUpgrade when an existing DB is found

    static final String DATABASE_NAME = "UploadQueue.db";   //filename of the actual DB in app's
                                                            //private storage (should probably be a
                                                            //constant)

    //some stored SQL names etc. for convenience
    static final String TABLE_QUOTES = "quote";
    static final String CREATE_QUOTES_TABLE =
        "CREATE TABLE " + TABLE_QUOTES + " ("
            + QuoteSchema.UUID + " BLOB PRIMARY KEY,"
            + QuoteSchema.QUOTE + " BLOB,"
            + QuoteSchema.RETRY_AT + " INTEGER,"
            + QuoteSchema.RETRIES + " INTEGER)";

    //will be our db connection - static so that it's available to any instance of this class
    //thus avoiding having multiple connections open (even though SQLite is thread safe)
    static SQLiteDatabase db = null;

    //default constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //default constructor
    public DBHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, errorHandler);
    }

    /**
     * Get the current db connection or create one
     * @return
     */
    protected SQLiteDatabase getDatabase(){
        if(db == null){
            db = getWritableDatabase();
        }

        return db;
    }

    /**
     * Called when the DB file is created for the first time
     * @param db The DB instance to operate on
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUOTES_TABLE);
    }

    /**
     * Called when there is an existing DB file but the version number is different to the
     * current (see DATABASE_VERSION)
     * @param db The DB instance to operate on
     * @param oldVersion The existing file's version number
     * @param newVersion The version number to upgrade to
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* we'll put upgrade statements in here later if we alter the schema */
    }

    /**
     * Insert or replace a QuoteUpload object
     * @param quoteUpload Object to insert
     * @throws Model.ModelException
     * @throws SQLException

    public void insertReplaceQuoteUpload(QuoteUpload quoteUpload) throws Model.ModelException, SQLException{
        String quoteJson = quoteUpload.quote.toJSONObject().toString();

        ContentValues values = new ContentValues(); //the values to insert
        values.put(QuoteSchema.UUID, quoteUpload.uuid);
        values.put(QuoteSchema.QUOTE, quoteJson);
        values.put(QuoteSchema.RETRIES, quoteUpload.retries);
        values.put(QuoteSchema.RETRY_AT, quoteUpload.retryAt);

        getDatabase().replaceOrThrow(TABLE_QUOTES, null, values);
    }
     */
    /**
     * Get the QuoteUpload object which has the lowest "retryAt" value
     * @return
     * @throws Model.ModelException
     * @throws SQLException

    public QuoteUpload getQuoteUploadWithSoonestRetry() throws Model.ModelException, SQLException{
        //the columns we want in the order we want them
        String projection[] = {
                QuoteSchema.UUID,
                QuoteSchema.QUOTE,
                QuoteSchema.RETRIES,
                QuoteSchema.RETRY_AT
        };

        //order by
        String orderBy = QuoteSchema.RETRY_AT + " DESC";

        //limit
        String limit = "1";

        //get a Cursor to iterate with
        Cursor c = getDatabase().query(TABLE_QUOTES, projection, null, null, null, null, orderBy, limit);

        if(c.getCount() == 0){
            return null;
        }

        //go to the first record
        c.moveToFirst();

        return makeQuoteUpload(c);
    }
     */
    /**
     * Helper function to take a cursor and populate a QuoteUpload object from it
     * @param c The cursor to use - must be at the position where the required object row is
     * @return
     * @throws Model.ModelException
     * @throws SQLException

    protected QuoteUpload makeQuoteUpload(Cursor c) throws Model.ModelException, SQLException{
        QuoteUpload quoteUpload = new QuoteUpload();

        quoteUpload.uuid = c.getString(c.getColumnIndexOrThrow(QuoteSchema.UUID));
        quoteUpload.retries = c.getInt(c.getColumnIndexOrThrow(QuoteSchema.RETRIES));
        quoteUpload.retryAt = c.getInt(c.getColumnIndexOrThrow(QuoteSchema.RETRY_AT));
        String quoteJson = c.getString(c.getColumnIndexOrThrow(QuoteSchema.QUOTE));

        Quote q = new Quote();
        q.populate(quoteJson);

        quoteUpload.quote = q;

        return quoteUpload;
    }
     */
    /**
     * Get a QuoteUpload object by it's UUID, retryAt value or both
     * @param uuid If not null only retrieve a QuoteUpload with this UUID
     * @param retryAtLessThan If not null only return a QuoteUpload with a retryAt value less than this
     * @return
     * @throws Model.ModelException
     * @throws SQLException

    public QuoteUpload getQuoteUpload(String uuid, long retryAtLessThan) throws Model.ModelException, SQLException{
        //the columns we want in the order we want them
        String projection[] = {
            QuoteSchema.UUID,
            QuoteSchema.QUOTE,
            QuoteSchema.RETRIES,
            QuoteSchema.RETRY_AT
        };

        //where statement
        String selection = "";

        //values to escape into the where statement (in the order of the placeholders)
        List<String> selectionArgs = new ArrayList<>();

        if(uuid != null){
            selection += QuoteSchema.UUID + " = ?";
            selectionArgs.add(uuid);
        }

        if(retryAtLessThan != -1){
            if(selection.length() > 0){
                selection += " AND ";
            }
            selection += QuoteSchema.RETRY_AT + " < ?";
            selectionArgs.add(retryAtLessThan + "");
        }

        //convert selection args to a simple array for SQLite
        String[] selectionArgsArr = selectionArgs.toArray(new String[selectionArgs.size()]);

        //get a cursor to iterate with
        Cursor c = getDatabase().query(TABLE_QUOTES, projection, selection, selectionArgsArr, null, null, null);

        if(c.getCount() == 0){
            return null;
        }

        //go to the first record
        c.moveToFirst();

        return makeQuoteUpload(c);
    }
     */
    /**
     * Get a QuoteUpload object by UUID
     * @param uuid The UUID of the QuoteUpload required
     * @return
     * @throws Model.ModelException
     * @throws SQLException

    public QuoteUpload getQuoteUpload(String uuid) throws Model.ModelException, SQLException {
        return getQuoteUpload(uuid, -1);
    }
     */
    /**
     * Get a quote upload object with a retryAt value less than a certain amount
     * @param retryAtLessThan Only return a QuoteUpload object with a retryAt value less than this
     * @return
     * @throws Model.ModelException
     * @throws SQLException

    public QuoteUpload getQuoteUpload(long retryAtLessThan) throws Model.ModelException, SQLException{
        return getQuoteUpload(null, retryAtLessThan);
    }
     */
    /**
     * Delete a quote upload object
     * @param uuid The UUID of the QuoteUpload object to delete
     * @throws SQLException

    public void deleteQuoteUpload(String uuid) throws SQLException{
        String selection = QuoteSchema.UUID + " = ?";
        String[] selectionArgs = {uuid};

        getDatabase().delete(TABLE_QUOTES, selection, selectionArgs);
    }
     */
    /**
     * Count the total number of QuoteUploads
     * @return
     * @throws SQLException

    public int countQuoteUploads() throws SQLException{
        //the columns we want in the order we want them
        String projection[] = {
                QuoteSchema.UUID,
                QuoteSchema.QUOTE,
                QuoteSchema.RETRIES,
                QuoteSchema.RETRY_AT
        };

        //get a cursor simply to query it's count
        Cursor c = getDatabase().query(TABLE_QUOTES, projection, null, null, null, null, null);

        return c.getCount();
    }
     */

    public static abstract class QuoteSchema{
        public static final String UUID = "uuid";
        public static final String QUOTE = "quote";
        public static final String RETRY_AT = "retry_at";
        public static final String RETRIES = "retries";
    }

}
