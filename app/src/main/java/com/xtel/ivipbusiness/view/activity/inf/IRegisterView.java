package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by VULCL on 1/10/2017
 */

public interface IRegisterView {

    void onValidateError(String error);
    void onRegisterAccount(String phone, String password);
    void onRegisterAccountSuccess();
    void onValidatePhoneToActiveSuccess(String auth_id);
    void startActivityForResult(Intent intent, int request);
    Activity getActivity();
}
