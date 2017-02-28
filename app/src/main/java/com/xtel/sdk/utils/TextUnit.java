package com.xtel.sdk.utils;

import android.text.TextUtils;

/**
 * Created by Vulcl on 10-Feb-17
 */

public class TextUnit {
    private static TextUnit instance;

    public static TextUnit getInstance() {
        if (instance == null)
            instance = new TextUnit();
        return instance;
    }

    public boolean validateText(String text) {
        return (text != null && !text.isEmpty());
    }

    public int validateInteger(String _long) {
        try {
            return Integer.parseInt(_long);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public long validateLong(String _long) {
        try {
            return Long.parseLong(_long);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public double validateDouble(String _double) {
        try {
            return Double.parseDouble(_double);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean validatePhone(String username) {
        return validateInteger(username) != -1 && !(username.length() < 10 || username.length() > 11);
    }

    public boolean validateEmail(String text) {
        return !TextUtils.isEmpty(text) && android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }
}
