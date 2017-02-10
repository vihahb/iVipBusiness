package com.xtel.sdk.utils;

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

    public long isPhone(String username) {
        try {
            return Long.parseLong(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean validatePhone(String username) {
        return !(username.length() < 10 || username.length() > 11);
    }

    public boolean validateEmail(String text) {
        return !android.text.TextUtils.isEmpty(text) && android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }
}
