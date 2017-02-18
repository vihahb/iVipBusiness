package com.xtel.ivipbusiness.view.fragment.inf;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public interface IStoresView {

    void onGetDataError();
    void getNewSession(ICmd iCmd);

    void onLoadMore();
    void onGetStoresSuccess(ArrayList<SortStore> arrayList);
    void onGetStoresError(Error error);
    void onNoNetwork();
    void startActivity(Class clazz, String key, Object object, int requestCode);
    Activity getActivity();
    Fragment getFragment();
}
