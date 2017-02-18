package com.xtel.ivipbusiness.view.fragment.inf;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.xtel.ivipbusiness.model.entity.News;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/21/2017
 */

public interface INewsView {

    void onGetDataError();
    void onLoadMore();
    void onGetNewsSuccess(ArrayList<News> arrayList);
    void onRequestError(Error error);
    void getNewSession(ICmd iCmd, int type);
    void onNoNetwork();
    Activity getActivity();
    Fragment getFragment();
}