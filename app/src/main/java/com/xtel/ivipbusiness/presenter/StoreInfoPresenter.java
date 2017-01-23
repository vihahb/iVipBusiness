package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.ivipbusiness.view.fragment.inf.IStoreInfoView;
import com.xtel.nipservice.callback.ResponseHandle;
import com.xtel.nipservice.model.entity.Error;

/**
 * Created by Vulcl on 1/21/2017
 */

public class StoreInfoPresenter {
    private IStoreInfoView view;

    public StoreInfoPresenter(IStoreInfoView view) {
        this.view = view;
    }

    public void getStoreInfo() {
        StoresModel.getInstance().getStoreInfo(new ResponseHandle<RESP_Store>(RESP_Store.class) {
            @Override
            public void onSuccess(RESP_Store obj) {
                view.onGetStoreInfoSuccess(obj);
            }

            @Override
            public void onError(Error error) {
                view.onGetStoreInfoError();
            }
        });
    }
}