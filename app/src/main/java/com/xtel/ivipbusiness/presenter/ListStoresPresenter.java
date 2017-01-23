package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.RESP_List_Sort_Store;
import com.xtel.ivipbusiness.view.activity.inf.IListStoreView;
import com.xtel.nipservice.callback.ResponseHandle;
import com.xtel.nipservice.model.entity.Error;
import com.xtel.nipservice.model.entity.RESP_Basic;
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

        StoresModel.getInstance().getListStoreNotInChain(new ResponseHandle<RESP_List_Sort_Store>(RESP_List_Sort_Store.class) {
            @Override
            public void onSuccess(RESP_List_Sort_Store obj) {
                view.onGetListStoresSuccess(obj.getData());
            }

            @Override
            public void onError(Error error) {
                view.onGetListStoresError(error);
            }
        });
    }
}
