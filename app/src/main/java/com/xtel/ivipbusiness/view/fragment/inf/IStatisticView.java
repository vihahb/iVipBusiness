package com.xtel.ivipbusiness.view.fragment.inf;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.xtel.ivipbusiness.model.entity.DataObj;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Vulcl on 3/10/2017
 */

public interface IStatisticView {

    void onGetStoresSuccess(ArrayList<SortStore> arrayList);
    void onGetStatistic(int action, ArrayList<DataObj> arrayList);
    void getNewSession(ICmd iCmd, Object... param);
    void onRequestError(Error error);

    void onNoInternet();
    Fragment getFragment();
    Activity getActivity();
}