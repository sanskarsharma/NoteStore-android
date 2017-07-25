package dev.sanskar.com.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sanskar on 4/14/2017.
 */
public class DBHandler extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "notesDB";

    // Notes table name
    private static final String TABLE_NOTES = "notes2";

    // Notes Table Columns names
    private static final String KEY_NOTE = "note";
    private static final String KEY_CREATEDAT = "createdAt";
    private static final String KEY_USER_MOBILE_NUMBER = "userMobile";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_CREATEDAT + " TEXT PRIMARY KEY," + KEY_NOTE + " TEXT, "+KEY_USER_MOBILE_NUMBER+" TEXT "+ ")";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);

        // Create tables again
        onCreate(db);
    }



    public void addNote(NoteModel noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //values.put(KEY_CREATEDAT, noteModel.getCreatedAt()); // Note is created at
        values.put(KEY_NOTE, noteModel.getThenote()); // Note data
        values.put(KEY_USER_MOBILE_NUMBER, noteModel.getUserMobile()); // Note created by

        // Inserting Row
        db.insert(TABLE_NOTES, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Notes of a User
    public List<NoteModel> getAllNotes(String userMobileNumber) {
        List<NoteModel> noteslist = new ArrayList<NoteModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_NOTES+ " WHERE "+ KEY_USER_MOBILE_NUMBER+" = "+ userMobileNumber;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NoteModel notu = new NoteModel();
                notu.setCreatedAt(cursor.getString(0));
                notu.setThenote(cursor.getString(1));
                notu.setUserMobile(cursor.getString(2));
                // Adding note to list
                noteslist.add(notu);
            } while (cursor.moveToNext());
        }

        // return notes list
        return noteslist;
    }

    // Deleting note
    public void deleteContact(NoteModel noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_CREATEDAT + " = ?",
                new String[]{String.valueOf(noteModel.getCreatedAt())});
        db.close();
    }

    // Updating note
    public int updateContact(NoteModel noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CREATEDAT, noteModel.getCreatedAt());
        values.put(KEY_NOTE, noteModel.getThenote());
        values.put(KEY_USER_MOBILE_NUMBER, noteModel.getUserMobile());


        // updating row
        return db.update(TABLE_NOTES, values, KEY_CREATEDAT + " = ?",
                new String[] { String.valueOf(noteModel.getCreatedAt()) });
    }



    // Getting notes Count
    // unused function
    public int getNotesCount(String mobileNumber) {
        String countQuery = "SELECT  * FROM " + TABLE_NOTES+" WHERE "+KEY_USER_MOBILE_NUMBER+"="+ " '"+mobileNumber+"' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

}
