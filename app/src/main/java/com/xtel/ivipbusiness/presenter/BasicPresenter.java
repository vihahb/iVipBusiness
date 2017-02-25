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
}