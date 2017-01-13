package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.Stores;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public interface IListStoreView {

    void onGetStoresSuccess(ArrayList<Stores> arrayList);
    void onNoNetwork();
    Activity getActivity();
}
