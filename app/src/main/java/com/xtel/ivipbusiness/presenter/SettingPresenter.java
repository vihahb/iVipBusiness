package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.News;
import com.xtel.ivipbusiness.model.entity.RESP_Setting;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.activity.inf.ISettingView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;
import com.xtel.sdk.commons.Constants;

/**
 * Created by Vulcl on 3/1/2017
 */

public class SettingPresenter {
    private ISettingView view;

    private SortStore sortStore;
    private final String STORE_TYPE = "STORE";

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = (int) params[0];

                if (type == 1)
                    StoresModel.getInstance().getStoreSetting(sortStore.getId(), sortStore.getStore_type(), new ResponseHandle<RESP_Setting>(RESP_Setting.class) {
                        @Override
                        public void onSuccess(RESP_Setting obj) {
                            view.onGetSettingSuccess(obj);
                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd, params);
                            else
                                view.onGetDataError();
                        }
                    });
            }
        }
    };

    public SettingPresenter(ISettingView view) {
        this.view = view;
    }

    public void getData() {
        try {
            sortStore = (SortStore) view.getActivity().getIntent().getSerializableExtra(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sortStore == null)
            view.onGetDataError();
        else {
            view.onGetDataSuccess((!sortStore.getStore_type().equals(STORE_TYPE)));
            iCmd.execute(1);
        }
    }
}