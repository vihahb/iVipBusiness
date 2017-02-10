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

    protected boolean validateText(String text) {
        return (text != null && !text.isEmpty());
    }

    protected long isPhone(String username) {
        try {
            return Long.parseLong(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    protected boolean validatePhone(String username) {
        return !(username.length() < 10 || username.length() > 11);
    }

    protected boolean validateEmail(String text) {
        return !android.text.TextUtils.isEmpty(text) && android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }
}
