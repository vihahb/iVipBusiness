package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

/**
 * Created by Lê Công Long Vũ on 12/27/2016
 */

public interface IHomeView {

    void onGetShortUserDataSuccess(RESP_Full_Profile obj);
    void onGetShortUserDataError(Error error);
    void getNewSession(ICmd iCmd, int type);
    Activity getActivity();
}
