package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Mr. M.2 on 1/12/2017
 */

public interface IActiveView {

    void onValidateError(String error);
    void onValidatePhoneToActiveSuccess(String auth_id);
    void onNoInternet();

    void startActivityAndFinish(Class clazz);
    void startActivityForResult(Intent intent, int request);
    Activity getActivity();
}
