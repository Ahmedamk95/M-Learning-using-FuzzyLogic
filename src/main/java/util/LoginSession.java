package util;

import android.content.Context;
import android.content.SharedPreferences;

import com.excel.excelclasslibrary.UtilSharedPreferences;

/**
 * Created by Sohail on 15-04-2017.
 */

public class LoginSession {

    final static String LOGIN_SESSION = "login";
    final static String IS_LOGGED_IN = "is_logged_in";
    final static String USERNAME = "username";
    final static String ROLE_NAME = "role";
    final static String SEMESTER = "semester";

    public static boolean isLoggedIn( Context context ){
        SharedPreferences spfs = UtilSharedPreferences.createSharedPreference( context, LOGIN_SESSION );
        String is_logged_in = (String) UtilSharedPreferences.getSharedPreference( spfs, IS_LOGGED_IN, "false" );
        if( is_logged_in.equals( "true" ) )
            return true;

        return false;
    }

    public static void logOut( Context context ){
        SharedPreferences spfs = UtilSharedPreferences.createSharedPreference( context, LOGIN_SESSION );
        UtilSharedPreferences.editSharedPreference( spfs, IS_LOGGED_IN, "false" );
    }

    public static void hasLoggedIn( Context context, String username, String role, String semester ){
        SharedPreferences spfs = UtilSharedPreferences.createSharedPreference( context, LOGIN_SESSION );
        UtilSharedPreferences.editSharedPreference( spfs, IS_LOGGED_IN, "true" );
        UtilSharedPreferences.editSharedPreference( spfs, USERNAME, username );
        UtilSharedPreferences.editSharedPreference( spfs, ROLE_NAME, role );
        UtilSharedPreferences.editSharedPreference( spfs, SEMESTER, semester );
    }

    public static String getRoleOfLoggedInUser( Context context ){
        SharedPreferences spfs = UtilSharedPreferences.createSharedPreference( context, LOGIN_SESSION );
        return (String) UtilSharedPreferences.getSharedPreference( spfs, ROLE_NAME, "student" );
    }

    public static String getUsernameOfLoggedInUser( Context context ){
        SharedPreferences spfs = UtilSharedPreferences.createSharedPreference( context, LOGIN_SESSION );
        return (String) UtilSharedPreferences.getSharedPreference( spfs, USERNAME, "aaaaa1" );
    }

    public static String getSemesterOfLoggedInUser( Context context ){
        SharedPreferences spfs = UtilSharedPreferences.createSharedPreference( context, LOGIN_SESSION );
        return (String) UtilSharedPreferences.getSharedPreference( spfs, SEMESTER, "sem1" );
    }
}
