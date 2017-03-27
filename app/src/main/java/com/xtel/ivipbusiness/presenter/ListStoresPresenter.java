package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.RESP_List_Sort_Store;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.activity.inf.IListStoreView;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.commons.NetWorkInfo;

import java.util.ArrayList;

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
            public void onSuccess() {

            }

            @Override
            public void onError(Error error) {
                view.onGetListStoresError(error);
            }
        });
    }

    public void chooseList(ArrayList<SortStore> listData) {
        ArrayList<Integer> arrayList = new ArrayList<>();

        for (int i = listData.size() - 1; i >= 0; i--) {
            if (listData.get(i).isCHecked())
                arrayList.add(listData.get(i).getId());
        }

        if (arrayList.size() == 0) {
            view.showShortToast(view.getActivity().getString(R.string.error_no_store_selected));
            return;
        }


    }
}
