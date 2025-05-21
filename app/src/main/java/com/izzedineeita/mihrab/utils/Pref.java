package com.izzedineeita.mihrab.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Izzedine eita on 26/11/2020.
 * project name : mihrab
 * Email : izzedineeita@gmail.com
 * WebSite : izzedineeita.com & Kotdroid.com
 */

public class Pref {

    private static final String FILE_NAME = "MIHRAB_DATA";
    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences.Editor editor;


    private static void openShare(Context context) {
        sharedPreferences = context.getSharedPreferences
                (FILE_NAME,Context.MODE_PRIVATE);
    }

    private static void closeShare() {
        sharedPreferences = null;
    }
    
    public static void setValue(Context context, String key, String value) {
        openShare(context);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        editor = null;
        closeShare();
    }

    public static void setValue(Context context, String key, int value) {
        openShare(context);
        editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
        editor = null;
        closeShare();
    }

    public static void setValue(Context context, String key, boolean value) {
        openShare(context);
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
        editor = null;
        closeShare();
    }

    public static String getValue(Context context, String key, String value) {
        openShare(context);
        String result = sharedPreferences.getString(key, value);
        closeShare();
        return result;
    }

    public static boolean getValue(Context context, String key, boolean value) {
        openShare(context);
        boolean result = sharedPreferences.getBoolean(key, value);
        closeShare();
        return result;
    }

    public static int getValue(Context context, String key, int value) {
        openShare(context);
        int result = sharedPreferences.getInt(key, value);
        closeShare();
        return result;
    }
}
