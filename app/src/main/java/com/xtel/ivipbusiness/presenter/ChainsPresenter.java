package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.entity.RESP_List_Sort_Store;
import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.view.fragment.inf.IChainsView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class ChainsPresenter extends BasicPresenter {
    private IChainsView view;

    private final String TYPE = "ALL";
    private int PAGE = 1;
    private int PAGESIZE = 10;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            if (params.length > 0) {
                if ((int) params[0] == 1)
                    StoresModel.getInstance().getListChains(TYPE, PAGE, PAGESIZE, new ResponseHandle<RESP_List_Sort_Store>(RESP_List_Sort_Store.class) {
                        @Override
                        public void onSuccess(RESP_List_Sort_Store obj) {
                            PAGE++;
                            view.onGetStoresSuccess(obj.getData());
                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd);
                            else
                                view.onGetStoresError(error);
                        }
                    });
            }
        }
    };

    public ChainsPresenter(IChainsView view) {
        this.view = view;
    }

    public void getChains(boolean isClear) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        if (isClear)
            PAGE = 1;

        iCmd.execute(1);
    }
}