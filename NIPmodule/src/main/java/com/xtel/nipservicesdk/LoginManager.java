package com.xtel.nipservicesdk;

import android.content.Context;

import com.xtel.nipservicesdk.model.LoginModel;

/**
 * Created by Lê Công Long VŨ on 1/10/2017
 */

public class LoginManager {

    public static void sdkInitialize(Context context) {
        NipApplication.context = context;
    }

    public static String getCurrentSession() {
        return LoginModel.getInstance().getSession();
    }

    public static void logOut() {
        LoginModel.getInstance().logout();
    }

    public static String getCurrentAuthenticationId() {
        return LoginModel.getInstance().getCurrentAuthenticationId();
    }
}