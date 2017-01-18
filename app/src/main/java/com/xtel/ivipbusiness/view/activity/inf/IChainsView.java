package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public interface IChainsView {

    void onGetStoresSuccess(ArrayList<SortStore> arrayList);
    void onGetStoresError(Error error);
    void onNoNetwork();
    Activity getActivity();
}
