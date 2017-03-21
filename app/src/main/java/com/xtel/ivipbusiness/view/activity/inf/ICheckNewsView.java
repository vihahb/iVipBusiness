package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.RESP_Valid_Check_News;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

/**
 * Created by Vulcl on 3/20/2017
 */

public interface ICheckNewsView {

    void onValidCheckSuccess(RESP_Valid_Check_News obj);
    void onUseVoucherSuccess();
    void onRequestError(Error error);
    void getNewSession(ICmd iCmd, Object... params);

    void showShortToast(String message);
    Activity getActivity();
}
