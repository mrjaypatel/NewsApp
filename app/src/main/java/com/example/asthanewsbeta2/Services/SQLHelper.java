package com.example.asthanewsbeta2.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
    //Database Basic Configuration
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "OFFLINE_DATA";
    private static final String OFFLINE_POST = "OFFLINE_POST";
    private static final String OFFLINE_MENU = "OFFLINE_MENU";

    //Define OFFLINE_POST TABLE STRUCTURE
    private static final String POST_INDEX_ID = "index_id";
    private static final String POST_ID = "id";
    private static final String POST_TITLE = "title";
    private static final String POST_DETAILS = "details";
    private static final String POST_DATEANDTIME = "dateandtime";
    private static final String POST_VIEWS = "views";
    private static final String POST_POSTCODE = "postcode";


    //Log Tag
    final String TAG = "SQLHelper";


    public SQLHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        /* Creating table for storing offline post
         * For fast data providing.
         */
        String OFFLINE_POST_CMD = "CREATE TABLE " + OFFLINE_POST + " ( " + POST_INDEX_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "" + POST_ID + " INT, " +
                "" + POST_TITLE + " TEXT, " +
                "" + POST_DETAILS + " TEXT, " +
                "" + POST_DATEANDTIME + " TEXT, " +
                "" + POST_VIEWS + " TEXT, " +
                "" + POST_POSTCODE + " TEXT )";
        db.execSQL(OFFLINE_POST_CMD);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Post Data Existence check
        String POST_DATA_Str = "DROP TABLE IF EXISTS " + OFFLINE_POST;
        db.execSQL(POST_DATA_Str);

    }


    //Add Offline Post To the database Table offline Post
    public void addOfflinePost(int ADD_ID, String ADD_TITLE, String ADD_DETAILS, String ADD_DATEANDTIME, String ADD_VIEWS, String ADD_POSTCODE) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLHelper.POST_ID, ADD_ID);
        values.put(SQLHelper.POST_TITLE, ADD_TITLE);
        values.put(SQLHelper.POST_DETAILS, ADD_DETAILS);
        values.put(SQLHelper.POST_DATEANDTIME, ADD_DATEANDTIME);
        values.put(SQLHelper.POST_VIEWS, ADD_VIEWS);
        values.put(SQLHelper.POST_POSTCODE, ADD_POSTCODE);
        db.insert(SQLHelper.OFFLINE_POST, null, values);
    }


    public String[] getOfflinePost(int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {SQLHelper.POST_ID, SQLHelper.POST_TITLE, SQLHelper.POST_DETAILS, SQLHelper.POST_DATEANDTIME, SQLHelper.POST_VIEWS, SQLHelper.POST_POSTCODE};
        Cursor cursor = db.query(SQLHelper.OFFLINE_POST, projection, null, null, null, null, null);
        String[] data = new String[countRecord(OFFLINE_POST)];
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                data[i] = cursor.getString(index);
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }


   /* private String getLastId() {
        String selectQuery = "SELECT  * FROM " + POST_DATA + " ORDER BY " + ID + " DESC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String data = "";
        if (cursor.moveToFirst()) {
            do {
                data = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }*/

    public int countRecord(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();
        return recordCount;
    }


    public String getPostDataById(int index, int post_index_id) {
        String selectQuery = "SELECT  * FROM " + OFFLINE_POST + " WHERE " + POST_INDEX_ID + " = " + post_index_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String data = "";
        if (cursor.moveToFirst()) {
            do {
                data = cursor.getString(index);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }


}
