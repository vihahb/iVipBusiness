package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.SortStore;

/**
 * Created by Vulcl on 2/11/2017
 */

public interface IViewStoreView {
    void onGetDataSuccess();
    void onGetDataError();
    Activity getActivity();
}