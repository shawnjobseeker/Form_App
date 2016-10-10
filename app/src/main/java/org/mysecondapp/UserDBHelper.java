package org.mysecondapp;

/**
 * Created by Shawn Li on 10/6/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // derived from http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PIC = "photo";
    private static final String KEY_NATION = "country";
    private static final String KEY_MF = "gender";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DOB = "date_of_birth";
    private final ArrayList<UserData> allRecords = new ArrayList<UserData>();

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT," + KEY_EMAIL + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_DOB + " TEXT," + KEY_PIC + " BLOB," + KEY_NATION + " TEXT," + KEY_MF + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    public void addRecord(UserData data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, data.getName()); // Contact Name
        values.put(KEY_PH_NO, data.getPhone()); // Contact Phone
        values.put(KEY_EMAIL, data.getEmail()); // Contact Email
        values.put(KEY_ADDRESS, data.getAddress());
        values.put(KEY_DOB, data.getDob());
        if (data.getImage() != null)
        values.put(KEY_PIC, data.getPicBlob());
        else
        values.put(KEY_PIC, (byte[])null);
        values.put(KEY_NATION, data.getCountry());
        values.put(KEY_MF, data.getGender());
        // Inserting Row
        db.insertOrThrow(TABLE_CONTACTS, null, values);
        db.close();
    }
    public void updateRecord(UserData data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, data.getName()); // Contact Name
        values.put(KEY_PH_NO, data.getPhone()); // Contact Phone
        values.put(KEY_EMAIL, data.getEmail()); // Contact Email
        values.put(KEY_ADDRESS, data.getAddress());
        values.put(KEY_DOB, data.getDob());
        if (data.getImage() != null)
            values.put(KEY_PIC, data.getPicBlob());
        else
            values.put(KEY_PIC, (byte[])null);
        values.put(KEY_NATION, data.getCountry());
        values.put(KEY_MF, data.getGender().substring(0, 1));
        // Inserting Row
        db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
        db.close();
    }
    public void deleteRecord(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
    public UserData getRecord(int id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] keys = {KEY_ID,
                    KEY_NAME, KEY_PH_NO, KEY_EMAIL, KEY_ADDRESS, KEY_DOB, KEY_PIC, KEY_NATION, KEY_MF};
            Cursor cursor = db.query(TABLE_CONTACTS, keys, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
            UserData data = new UserData();
            data.setAll(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getBlob(6), cursor.getString(7), cursor.getString(8));
            return data;
        }catch (CursorIndexOutOfBoundsException c)
        {
            return null;
        }
    }
    public ArrayList<UserData> getAllRecords() {

            allRecords.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            int c = cursor.getCount();
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    UserData data = new UserData();
                    data.setAll(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getBlob(6), cursor.getString(7), cursor.getString(8));
                    allRecords.add(data);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return allRecords;



    }
}
