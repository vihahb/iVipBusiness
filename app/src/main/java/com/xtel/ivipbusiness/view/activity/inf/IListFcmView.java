package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

/**
 * Created by Vulcl on 2/22/2017
 */

public interface IListFcmView {

    void onGetDataError();
    void onSendFcmSuccess();
    void onRequestError(Error error);
    void onNewsNotExists();

    void getNewSession(ICmd iCmd, Object... params);
    Activity getActivity();
}