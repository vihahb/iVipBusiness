package com.xtel.nipservice.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.xtel.nipservice.NipApplication;
import com.xtel.nipservice.commons.Cts;

/**
 * Created by Lê Công Long Vũ on 11/4/2016
 */

public class SharedUtils {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static SharedUtils instance;

    public static SharedUtils getInstance() {
        if (instance == null)
            instance = new SharedUtils();
        return instance;
    }

    private SharedUtils() {
        sharedPreferences = NipApplication.context.getSharedPreferences(Cts.SHARED_NAME, Context.MODE_PRIVATE);
    }

    @SuppressLint("CommitPrefEdits")
    private void prepair() {
        editor = sharedPreferences.edit();
    }

    public void putLongValue(String name, long value) {
        if (editor == null)
            prepair();
        editor.putLong(name, value);
        editor.commit();
    }

    public long getLongValue(String name) {
        return sharedPreferences.getLong(name, -1);
    }

    public void putStringValue(String name, String value) {
        if (editor == null)
            prepair();
        editor.putString(name, value);
        editor.commit();
    }

    public String getStringValue(String name) {
        return sharedPreferences.getString(name, null);
    }

    public void putIntValue(String name, int value) {
        if (editor == null)
            prepair();
        editor.putInt(name, value);
        editor.commit();
    }

    public int getIntValue(String name) {
        return sharedPreferences.getInt(name, -1);
    }

    public void putBooleanValue(String name, boolean value) {
        if (editor == null)
            prepair();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public boolean getBooleanValue(String name) {
        return sharedPreferences.getBoolean(name, false);
    }

    public void clearData() {
        if (editor == null)
            prepair();
        editor.clear();
        editor.commit();
    }

    public void remove(String key) {
        if (editor == null)
            prepair();
        editor.remove(key);
        editor.commit();
    }
}
