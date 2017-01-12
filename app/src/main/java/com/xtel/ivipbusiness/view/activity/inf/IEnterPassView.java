package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

/**
 * Created by Mr. M.2 on 1/12/2017.
 */

public interface IEnterPassView {

    void onGetDataError();
    void onValidateError(String error);
    void onResetPassWord(String auth_id, String password);
    Activity getActivity();
}
