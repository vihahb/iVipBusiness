package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.nipservice.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/17/2017
 */

public interface IListStoreView {

    void onLoadMore();
    void onGetListStoresSuccess(ArrayList<SortStore> arrayList);
    void onGetListStoresError(Error error);
    void showShortToast(String message);
    void onNoNetwork();
    Activity getActivity();
}
