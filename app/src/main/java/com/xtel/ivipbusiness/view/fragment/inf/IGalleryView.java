package com.xtel.ivipbusiness.view.fragment.inf;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.xtel.ivipbusiness.model.entity.Gallery;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public interface IGalleryView {

    void onGetDataError();
    void getNewSession(ICmd iCmd, Object... parame);

    void onLoadMore();
    void onGetStoresSuccess(ArrayList<Gallery> arrayList);
    void onGetStoresError(Error error);
    void onDeleteSuccess();
    void onRequestError(Error error);
    void onDeleteGallery(int id, int position);
    void onNoNetwork();
    void startActivity(Class clazz, String key, Object object);
    Activity getActivity();
    Fragment getFragment();
}
