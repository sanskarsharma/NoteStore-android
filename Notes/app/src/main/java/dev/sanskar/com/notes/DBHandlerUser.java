package dev.sanskar.com.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sanskar on 4/15/2017.
 */
public class DBHandlerUser extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "userDB";

    // user table name
    private static final String TABLE_USER = "user";

    // user Table Columns names
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NAME = "name";


    public DBHandlerUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_MOBILE + " TEXT PRIMARY KEY," + KEY_PASSWORD + " TEXT, "+ KEY_NAME +" TEXT "+ ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    public void addUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_MOBILE, userModel.getMobileNumber());
        values.put(KEY_PASSWORD, userModel.getPassword());
        values.put(KEY_NAME, userModel.getName());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }


    // Getting All users
    public List<UserModel> getAllUsers() {
        List<UserModel> userList = new ArrayList<UserModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserModel user = new UserModel();
                user.setMobileNumber(cursor.getString(0));
                user.setPassword(cursor.getString(1));
                user.setName(cursor.getString(2));

                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return user list
        return userList;
    }

    // Getting single contact
    public UserModel getUser(String mobileNo) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_MOBILE,
                        KEY_PASSWORD, KEY_NAME}, KEY_MOBILE + "=?",
                new String[]{String.valueOf(mobileNo)}, null, null, null, null);


        if (cursor != null){
            cursor.moveToFirst();
        }
        else{
            return null;
        }

        try {
            UserModel user = new UserModel(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2));

            return user;
        }catch (CursorIndexOutOfBoundsException e){ // in case user does not exist
            return null;
        }

    }


    // Deleting user account
    public void deleteContact(String mobileNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_MOBILE + " = ?",
                new String[] { String.valueOf(mobileNumber) });
        db.close();
    }

}
