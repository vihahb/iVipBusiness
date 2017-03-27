package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.RESP_List_Sort_Store;
import com.xtel.ivipbusiness.view.fragment.inf.IStoresView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.commons.NetWorkInfo;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class StoresPresenter {
    private IStoresView view;

    private boolean isExists = true;

    private final String TYPE = "ALL";
    private int PAGE = 1;
    private int PAGESIZE = 10;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            if (((int) params[0]) == 1)
                StoresModel.getInstance().getListStoreInChain(Constants.SORT_STORE.getId(), PAGE, PAGESIZE, new ResponseHandle<RESP_List_Sort_Store>(RESP_List_Sort_Store.class) {
                    @Override
                    public void onSuccess(RESP_List_Sort_Store obj) {
                        if (isExists) {
                            PAGE++;
                            view.onGetStoresSuccess(obj.getData());
                        }
                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Error error) {
                        if (isExists) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd);
                            else
                                view.onGetStoresError(error);
                        }
                    }
                });
        }
    };

    public StoresPresenter(IStoresView view) {
        this.view = view;
    }

    public void getStores(boolean isClear) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        if (!getData())
            view.onGetDataError();

        if (isClear)
            PAGE = 1;

        iCmd.execute(1);
    }

    public boolean getData() {
        return  (Constants.SORT_STORE != null);
    }

    public void setExists(boolean isExists) {
        this.isExists = isExists;
    }
}