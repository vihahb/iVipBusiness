package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.model.entity.StoresModel;
import com.xtel.ivipbusiness.view.activity.inf.IStoresView;
import com.xtel.sdk.utils.NetWorkInfo;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class StoresPresenter {
    private IStoresView view;

    public StoresPresenter(IStoresView view) {
        this.view = view;
    }

    public void getStores() {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        ArrayList<SortStore> arrayList = StoresModel.getInstance().getListStore();
        view.onGetStoresSuccess(arrayList);
    }
}