package com.example.social_photo.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//Class used to save some occurences of the account when the MainActivity is destroyed or paused.
public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    static final String PREF_PROPIC= "propic";
    static final String PREF_USER_ID= "userID";
    static final String PREF_USER_TOKEN= "userToken";
    static final String PREF_USER_ALBUMS= "albums";
    static final String PREF_USER_LAT= "lat";
    static final String PREF_USER_LNG= "lng";






    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    //-------------------------------
    //Used to retrieve the Real name of the user or to set it in the shared preferences
    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
    //--------------------------------


    //--------------------------------
    //Same as the real name of the user but in this case is used for the profile picture
    public static void setProfilePicture(Context ctx, String propic) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_PROPIC, propic);

        editor.commit();
    }
    //---------------------------------

    public static String getProfilePicture(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_PROPIC, "");
    }

    public static void setUserID(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, userName);
        editor.commit();
    }

    public static String getUserID(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }

    public static void setUserToken(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_TOKEN, userName);
        editor.commit();
    }

    public static String getUserToken(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_TOKEN, "");
    }

    public static void setUserAlbums(Context ctx, String albums) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ALBUMS, albums);
        editor.commit();
    }

    public static String getUserAlbums(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ALBUMS, "");
    }

    public static void setUserLat(Context ctx, String place) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_LAT, place);
        editor.commit();
    }

    public static String getUserLat(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_LAT, "");
    }

    public static void setUserLng(Context ctx, String place) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_LNG, place);
        editor.commit();
    }

    public static String getUserLng(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_LNG, "");
    }


}
