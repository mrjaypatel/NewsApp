package com.example.asthanewsbeta2.OfflineDataManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.asthanewsbeta2.Modules.GetMenu;
import com.example.asthanewsbeta2.Modules.GetPostFromLocal;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {
    //Database Basic Configuration
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "OFFLINE_DB";

    //Define OFFLINE_POST TABLE STRUCTURE
    private static final String OFFLINE_POST = "OFFLINE_POST";
    private static final String POST_INDEX_ID = "index_id";
    private static final String POST_ID = "id";
    private static final String POST_TITLE = "title";
    private static final String POST_IMGURL = "imgurl";
    private static final String POST_DETAILS = "details";
    private static final String POST_DATEANDTIME = "dateandtime";
    private static final String POST_VIEWS = "views";
    private static final String POST_POSTCODE = "postcode";
    private static final String POST_LNG = "lng";

    //Define column index for OFFLINE_POST
    int index_id = 0;
    int post_id = 1;
    int post_title = 2;
    int post_details = 3;
    int post_date = 4;
    int post_views = 5;
    int post_code = 6;
    int post_lng = 7;
    int post_imgurl = 8;


    //Define OFFLINE_MENU TABLE STRUCTURE
    private static final String OFFLINE_MENU = "OFFLINE_MENU";
    private static final String MENU_INDEX_ID = "index_id";
    private static final String MENU_ID = "id";
    private static final String MENU_TITLE = "title";
    private static final String MENU_CAT = "cat";
    private static final String MENU_LNG = "lng";

    //Define column index for OFFLINE_MENU
    int menu_index_id = 0;
    int menu_id = 1;
    int menu_title = 2;
    int menu_cat = 3;
    int menu_lng = 4;


    //Log Tag
    final String TAG = "HUMBINGO";


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
                "" + POST_IMGURL + " TEXT, " +
                "" + POST_DETAILS + " TEXT, " +
                "" + POST_DATEANDTIME + " TEXT, " +
                "" + POST_VIEWS + " TEXT, " +
                "" + POST_POSTCODE + " TEXT ," +
                "" + POST_LNG + " TEXT )";
        db.execSQL(OFFLINE_POST_CMD);

        String OFFLINE_MENU_CMD = "CREATE TABLE " + OFFLINE_MENU + " ( " + MENU_INDEX_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "" + MENU_ID + " INT, " +
                "" + MENU_TITLE + " TEXT, " +
                "" + MENU_CAT + " INT, " +
                "" + MENU_LNG + " TEXT )";
        db.execSQL(OFFLINE_MENU_CMD);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Post Data Existence check
        String POST_DATA_Str = "DROP TABLE IF EXISTS " + OFFLINE_POST;
        db.execSQL(POST_DATA_Str);

        //Post Data Existence check
        String MENU_DATA_Str = "DROP TABLE IF EXISTS " + OFFLINE_MENU;
        db.execSQL(MENU_DATA_Str);

    }

    /**
     * Custom methods for managing data as require
     * Doing CRUD operation into SQLite Database
     **/

    /**
     * Add Offline Post To the database Table offline Post
     */
    public void addOfflinePost(int ADD_ID, String ADD_TITLE, String ADD_IMGURL, String ADD_DETAILS, String ADD_DATEANDTIME, String ADD_VIEWS, String ADD_POSTCODE, String ADD_LNG) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLHelper.POST_ID, ADD_ID);
        values.put(SQLHelper.POST_TITLE, ADD_TITLE);
        values.put(SQLHelper.POST_IMGURL, ADD_IMGURL);
        values.put(SQLHelper.POST_DETAILS, ADD_DETAILS);
        values.put(SQLHelper.POST_DATEANDTIME, ADD_DATEANDTIME);
        values.put(SQLHelper.POST_VIEWS, ADD_VIEWS);
        values.put(SQLHelper.POST_POSTCODE, ADD_POSTCODE);
        values.put(SQLHelper.POST_LNG, ADD_LNG);
        db.insert(SQLHelper.OFFLINE_POST, null, values);
    }


    /**
     * Add Offline Menu To the database Table offline Post
     */
    public void addOfflineMenu(int ADD_ID, String ADD_TITLE, String ADD_CAT, String ADD_LNG) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLHelper.MENU_ID, ADD_ID);
        values.put(SQLHelper.MENU_TITLE, ADD_TITLE);
        values.put(SQLHelper.MENU_CAT, ADD_CAT);
        values.put(SQLHelper.MENU_LNG, ADD_LNG);
        db.insert(SQLHelper.OFFLINE_MENU, null, values);
    }


    /**
     * For Getting offline post string array {Note: This is structure for post only!}
     */
    private String[] getOffPostStr(int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {SQLHelper.POST_INDEX_ID, SQLHelper.POST_ID, SQLHelper.POST_TITLE, SQLHelper.POST_DETAILS, SQLHelper.POST_DATEANDTIME, SQLHelper.POST_VIEWS, SQLHelper.POST_POSTCODE, SQLHelper.POST_LNG, SQLHelper.POST_IMGURL};
        Cursor cursor = db.query(SQLHelper.OFFLINE_POST, projection, null, null, null, null, null);
        String[] data = new String[countRecord(OFFLINE_POST)];
        Log.d(TAG, "getOfflinePost: Total records: " + countRecord(OFFLINE_POST));
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                int i = 0;
                do {
                    data[i] = cursor.getString(index);
                    i++;
                } while (cursor.moveToNext());
            }
        } else {
            Log.d(TAG, "getOfflinePost: No Data Found! Something Wents Wrong!");
        }
        cursor.close();
        db.close();
        return data;
    }


    /**
     * For getting offline post array list for object response{Note: This is structure for post only!}
     */
    public List<GetPostFromLocal> getOffPostList(String postCode, String lng) {
        List<GetPostFromLocal> postData = new ArrayList<>();
        GetPostFromLocal item;
        String selectQuery = "SELECT * FROM " + OFFLINE_POST + " WHERE " + POST_POSTCODE + " = \"" + postCode + "\" AND " + POST_LNG + " = \"" + lng + "\" ORDER BY " + POST_ID + " ASC";
        Log.d(TAG, "getOffPostList: SQLString: " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                item = new GetPostFromLocal(
                        cursor.getString(post_id),
                        cursor.getString(post_title),
                        cursor.getString(post_details),
                        cursor.getString(post_date),
                        cursor.getString(post_views),
                        cursor.getString(post_code),
                        cursor.getString(post_lng),
                        cursor.getString(post_imgurl)
                );
                postData.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return postData;
    }


    public List<GetMenu> getOffMenuList(String lng) {
        List<GetMenu> menuItems = new ArrayList<>();
        GetMenu item;
        String selectQuery = "SELECT * FROM " + OFFLINE_MENU + " WHERE " + POST_LNG + " = \"" + lng + "\" ORDER BY " + MENU_ID + " ASC";
        Log.d(TAG, "getOfflineMenuList: SQLString: " + selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                item = new GetMenu(
                        cursor.getString(menu_id),
                        cursor.getString(menu_title),
                        cursor.getString(menu_cat),
                        cursor.getString(menu_lng)
                );
                menuItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menuItems;
    }


    //This method can find same post is exsist or not into the database
    public Boolean findPost(String postId, String postLng) {
        String sql = "SELECT  * FROM " + OFFLINE_POST + " WHERE " + POST_ID + " = " + postId + " AND " + POST_LNG + " = \"" + postLng + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        int recordCount = db.rawQuery(sql, null).getCount();
        Log.d(TAG, "findPost: " + countRecord("OFFLINE_POST"));
        return recordCount == 1;
    }


    //This method can find same post is exsist or not into the database
    public Boolean findMenu(String menuId, String menuLng) {
        String sql = "SELECT  * FROM " + OFFLINE_MENU + " WHERE " + MENU_ID + " = " + menuId + " AND " + MENU_LNG + " = \"" + menuLng + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        int recordCount = db.rawQuery(sql, null).getCount();
        return recordCount == 1;
    }

    //For cleaning up table data
    public boolean emptyTable(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
        db.execSQL("VACUUM");
        db.close();
        if (countRecord(TABLE_NAME) == 0) {
            return true;
        } else {
            return false;
        }
    }


    //For Adding New Column into table
    public void alterCol(String tablename, String col, String type) {
        String cmdStr = "ALTER TABLE " + tablename + " ADD " + col + " " + type;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(cmdStr);
    }


    //For getting last added ID from the table.
    private String getLastId(String TABLE, String COL) {
        String selectQuery = "SELECT  * FROM " + TABLE + " ORDER BY " + COL + " DESC ";
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
    }


    //Counting all existing row from the table
    public int countRecord(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;
        int recordCount = db.rawQuery(sql, null).getCount();
        return recordCount;
    }

    //Counting all existing rows for specific language
    public int countFiltPost(String postCode, String lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + OFFLINE_POST + " WHERE " + POST_POSTCODE + " = \"" + postCode + " AND " + POST_LNG + " = \"" + lng;
        int recordCount = db.rawQuery(sql, null).getCount();
        return recordCount;
    }


    //For getting specific index record by [ post_index_id ]
    public String getCellByIndexId(int index, int post_index_id) {
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

    //For getting specific index record by [ post_index_id ]
    public String getCellByPostId(int index, int post_id, String lng) {
        String selectQuery = "SELECT  * FROM " + OFFLINE_POST + " WHERE " + POST_ID + " = " + post_id + " AND " + POST_LNG + " =\"" + lng + "\"";
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


    //For getting all record by col_index
    private String getCell(int index) {
        String selectQuery = "SELECT  * FROM " + OFFLINE_POST;
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
