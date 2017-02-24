package com.xtel.ivipbusiness.presenter;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by VULCL on 1/10/2017
 */

abstract class BasicPresenter {

    protected void debug(String message) {
        Log.d(this.getClass().getSimpleName(), message);
    }

    boolean validateText(String text) {
        return (text != null && !text.isEmpty());
    }

    int validateInteger(String _long) {
        try {
            return Integer.parseInt(_long);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    long validateLong(String _long) {
        try {
            return Long.parseLong(_long);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    double validateDouble(String _double) {
        try {
            return Double.parseDouble(_double);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    boolean validatePhone(String username) {
        return !(username.length() < 10 || username.length() > 11);
    }

    boolean validateEmail(String text) {
        return !TextUtils.isEmpty(text) && android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }
}