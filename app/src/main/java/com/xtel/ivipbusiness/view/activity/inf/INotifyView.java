package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.Notify;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Vulcl on 2/22/2017
 */

public interface INotifyView {

    void onGetDataError();

    void onGetNotifySuccess(ArrayList<Notify> arrayList);
    void onSendFcmSuccess();
    void onRequestError(Error error);

    void getNewSession(ICmd iCmd, Object... params);
    Activity getActivity();
}