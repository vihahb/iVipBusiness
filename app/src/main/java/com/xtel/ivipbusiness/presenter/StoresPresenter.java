package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.entity.RESP_List_Sort_Store;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.view.activity.inf.IStoresView;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;
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

        StoresModel.getInstance().getListStoreNotInChain(new ResponseHandle<RESP_List_Sort_Store>(RESP_List_Sort_Store.class) {
            @Override
            public void onSuccess(RESP_List_Sort_Store obj) {
                view.onGetStoresSuccess(obj.getData());
            }

            @Override
            public void onError(Error error) {
                view.onGetStoresError(error);
            }
        });
    }
}