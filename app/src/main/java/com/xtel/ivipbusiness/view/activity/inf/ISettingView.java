package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.RESP_Setting;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

/**
 * Created by Vulcl on 3/1/2017
 */

public interface ISettingView {

    void onGetDataSuccess(boolean isChain);
    void onGetSettingSuccess(RESP_Setting resp_setting);
    void getNewSession(ICmd iCmd, Object... params);

    void onGetDataError();
    void onRequestError(Error error);

    Activity getActivity();
}
