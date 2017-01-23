package com.xtel.ivipbusiness.view.fragment.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.nipservice.model.entity.Error;

/**
 * Created by Vulcl on 1/21/2017
 */

public interface IStoreInfoView {

    void onGetStoreInfoSuccess(RESP_Store resp_store);
    void onGetStoreInfoError();
    Activity getActivity();
}