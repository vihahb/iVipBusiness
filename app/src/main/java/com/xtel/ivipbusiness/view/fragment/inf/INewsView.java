package com.xtel.ivipbusiness.view.fragment.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.News;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/21/2017
 */

public interface INewsView {

    void onLoadMore();
    void onGetNewsSuccess(ArrayList<News> arrayList);
    void onGetNewError(Error error);
    void onNoNetwork();
    Activity getActivity();
}