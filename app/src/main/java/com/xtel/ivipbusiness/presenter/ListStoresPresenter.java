package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.entity.StoresModel;
import com.xtel.ivipbusiness.view.activity.inf.IListStoreView;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Vulcl on 1/17/2017
 */

public class ListStoresPresenter {
    private IListStoreView view;

    public ListStoresPresenter(IListStoreView view) {
        this.view = view;
    }

    public void getListStores() {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        view.onGetListStoresSuccess(StoresModel.getInstance().getListStoreNotInChain());
    }
}
