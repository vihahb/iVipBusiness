package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

/**
 * Created by VULCL on 1/10/2017
 */

public interface IRegisterView {

    void onValidateError(String error);
    void onRegisterAccount(String phone, String password);
    Activity getActivity();
}
