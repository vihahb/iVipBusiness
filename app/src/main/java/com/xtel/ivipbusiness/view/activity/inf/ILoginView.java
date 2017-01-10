package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by VULCL on 1/10/2017
 */

public interface ILoginView {

    void onValidateError(String error);
    void loginAccount(String phone, String password);
    void startActivityAndFinish(Class clazz);
    Activity getActivity();
}
