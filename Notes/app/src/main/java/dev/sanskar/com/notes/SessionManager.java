package dev.sanskar.com.notes;

/**
 * Created by Sanskar on 4/15/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.Fragment;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "NOTES_prefs";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_NUMBER = "KEY_NUMBER";

    public static final String KEY_PASSWORD = "KEY_PASSWORD";

    public static final String KEY_NAME = "KEY_NAME";


    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

   // create new session
    public void createLoginSession(String mobileNumber, String password ,String name){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_NUMBER, mobileNumber);

        editor.putString(KEY_PASSWORD, password);

        editor.putString(KEY_NAME, name);

        // commit changes
        editor.commit();
    }


    public Boolean checkLogin(){                // returns true if user logged in , ese returns false
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
          return false;
        }
        else
            return true;

    }

    public String getUserMobileNumber(){

        return pref.getString(KEY_NUMBER,"");

    }


    public String getNameOfUser(){

        return pref.getString(KEY_NAME,"");

    }


    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();



    }


    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}