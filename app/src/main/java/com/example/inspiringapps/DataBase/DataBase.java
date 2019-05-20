package com.example.inspiringapps.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import com.example.inspiringapps.Model.Entry;
import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper{

    private static final String TAG = DataBase.class.getSimpleName();
    private static final String DB_NAME = "ApacheLogDB";
    private static final int DB_VER = 1;
    public static final String LOG_TABLE = "LogTable";
    public static final String COL_TIME_STAMP = "Timestamp";
    public static final String COL_IP_ADDRESS = "IPaddress";
    public static final String COL_WEB_PAGE = "WebPage";


    public DataBase(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        String createTable = "CREATE TABLE " + LOG_TABLE + "( " + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TIME_STAMP + " TEXT, " + COL_IP_ADDRESS + " TEXT, " + COL_WEB_PAGE + " TEXT )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade");
        String deleteTable = "DROP TABLE IF EXISTS " + LOG_TABLE;
        db.execSQL(deleteTable);
        onCreate(db);
    }

    /*Helper Methods.*/
    public void insertEntry(Entry entry) {
        /*Get the info needed from the boject.*/
        String timestamp = entry.getTimestamp();
        String ipAddress = entry.getIpAddress();
        String webPage = entry.getWebPage();
        /*Insert the info into the database.*/
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(COL_TIME_STAMP, timestamp);
        values.put(COL_IP_ADDRESS, ipAddress);
        values.put(COL_WEB_PAGE, webPage);
        db.insert(LOG_TABLE, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void clearTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ LOG_TABLE);
        db.close();
    }

    public ArrayList<String> getEntries() {

        String pages = "";
        ArrayList<String> listOfPages = new ArrayList<>();

        ArrayList<Entry> entries = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        //Query the database where the ip_address column is in asc order
        Cursor cursor = db.query(LOG_TABLE, new String[]{COL_TIME_STAMP, COL_IP_ADDRESS, COL_WEB_PAGE}, null, null, null, null, COL_IP_ADDRESS + " ASC");

        if (cursor.moveToFirst()) {
            do {
                String timestamp = cursor.getString(cursor.getColumnIndex(COL_TIME_STAMP));
                String ipaddress = cursor.getString(cursor.getColumnIndex(COL_IP_ADDRESS));
                String webpage = cursor.getString(cursor.getColumnIndex(COL_WEB_PAGE));
                //Create an entry with the info gather from the database
                Entry entry = new Entry(timestamp,ipaddress,webpage);
                //Add the entry to the list
                entries.add(entry);
            } while (cursor.moveToNext());
        }

        for(int entry=0;entry<entries.size()-2;entry++){
            String user1 = entries.get(entry).getIpAddress();
            String user2 = entries.get(entry+1).getIpAddress();
            String user3 = entries.get(entry+2).getIpAddress();

            if(user1.equals(user2) && user1.equals(user3)){
                String page1 = entries.get(entry).getWebPage();
                String page2 = entries.get(entry+1).getWebPage();
                String page3 = entries.get(entry+2).getWebPage();
                pages = page1+" -> "+page2+" -> "+page3;
                listOfPages.add(pages);
            }
        }

        cursor.close();

        return listOfPages;
    }

}
