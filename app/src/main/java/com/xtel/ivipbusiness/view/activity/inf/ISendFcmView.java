package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.LevelObject;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Vulcl on 2/22/2017
 */

public interface ISendFcmView {

    void onGetDataSuccess(int type);
    void onGetDataError();

    void onGetSettingSuccess(ArrayList<LevelObject> arrayList);

    void onRequestError(Error error);

    void getNewSession(ICmd iCmd);
    void showShortToast(int type, String message);

    Activity getActivity();
}