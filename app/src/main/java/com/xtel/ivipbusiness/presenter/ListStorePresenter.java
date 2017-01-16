package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.UserModel;
import com.xtel.ivipbusiness.model.entity.Stores;
import com.xtel.ivipbusiness.view.activity.inf.IListStoreView;
import com.xtel.sdk.utils.NetWorkInfo;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class ListStorePresenter {
    private IListStoreView view;

    public ListStorePresenter(IListStoreView view) {
        this.view = view;
    }

    public void getStores() {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        ArrayList<Stores> arrayList = UserModel.getIntances().getListStore();
        view.onGetStoresSuccess(arrayList);
    }
}