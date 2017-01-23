package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.entity.RESP_List_Sort_Store;
import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.view.fragment.inf.IChainsView;
import com.xtel.nipservice.callback.ResponseHandle;
import com.xtel.nipservice.model.entity.Error;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class ChainsPresenter {
    private IChainsView view;

    public ChainsPresenter(IChainsView view) {
        this.view = view;
    }

    public void getChains() {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        StoresModel.getInstance().getListChains(new ResponseHandle<RESP_List_Sort_Store>(RESP_List_Sort_Store.class) {
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
