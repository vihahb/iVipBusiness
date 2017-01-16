package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by VULCL on 1/10/2017
 */

public interface ILoginView {

    void onValidateError(String error);
    void loginAccount();
    void onValidatePhoneToResetSuccess(String auth_id);
    void onNoInternet();

    void startActivity(Class clazz);
    void startActivityForResult(Intent intent, int request);
    Activity getActivity();
}
