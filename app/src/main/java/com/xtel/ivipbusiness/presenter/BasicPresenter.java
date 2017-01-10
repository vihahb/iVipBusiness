package com.xtel.ivipbusiness.presenter;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by VULCL on 1/10/2017
 */

public abstract class BasicPresenter {

    protected void debug(String message) {
        Log.d(this.getClass().getSimpleName(), message);
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
        return !TextUtils.isEmpty(text) && android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }
}