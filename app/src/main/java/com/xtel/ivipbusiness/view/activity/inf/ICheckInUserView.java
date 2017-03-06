package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

/**
 * Created by Vulcl on 3/3/2017
 */

public interface ICheckInUserView {

    void onGetDataError();
    void getNewSession(ICmd iCmd, Object... params);
    void showShortToast(String messasge);

    void startActivityAndFinish(Class clazz, String key, Object object);
    void closeProgressBar();
    void onRequetServerError(String member_code, Error error);

    Activity getActivity();
}
