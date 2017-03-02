package com.xtel.ivipbusiness.presenter;

import android.util.Log;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.LevelObject;
import com.xtel.ivipbusiness.model.entity.News;
import com.xtel.ivipbusiness.model.entity.RESP_Setting;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.model.entity.TransferObject;
import com.xtel.ivipbusiness.view.activity.inf.ISettingView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.TextUnit;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by Vulcl on 3/1/2017
 */

public class SettingPresenter {
    private ISettingView view;

    private SortStore sortStore;
    private final String STORE_TYPE = "STORE";
    private boolean isExists = true;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = (int) params[0];

                if (type == 1)
                    StoresModel.getInstance().getStoreSetting(sortStore.getId(), sortStore.getStore_type(), new ResponseHandle<RESP_Setting>(RESP_Setting.class) {
                        @Override
                        public void onSuccess(RESP_Setting obj) {
                            if (isExists)
                                view.onGetSettingSuccess(obj);
                        }

                        @Override
                        public void onSuccess() {
                            if (isExists)
                                view.onGetSettingSuccess(null);
                        }

                        @Override
                        public void onError(Error error) {
                            if (isExists)
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onRequestError(error);
                        }
                    });
                else if (type == 2)
                    StoresModel.getInstance().addStoreSetting(JsonHelper.toJson(((RESP_Setting) params[1])), new ResponseHandle<RESP_None>(RESP_None.class) {
                        @Override
                        public void onSuccess(RESP_None obj) {
                            if (isExists)
                                view.onAddSettingSuccess();
                        }

                        @Override
                        public void onSuccess() {
                            if (isExists)
                                view.onAddSettingSuccess();
                        }

                        @Override
                        public void onError(Error error) {
                            if (isExists)
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onRequestError(error);
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

    public void addSetting(String to_money, String from_money, ArrayList<LevelObject> arrayList) {
        int to_point = TextUnit.getInstance().validateInteger(to_money);
        int from_point = TextUnit.getInstance().validateInteger(from_money);

        if (to_point == -1) {
            view.showShortToast(1, view.getActivity().getString(R.string.error_input_money));
            return;
        }
        if (from_point == -1) {
            view.showShortToast(2, view.getActivity().getString(R.string.error_input_money));
            return;
        }
        if (arrayList.size() == 0) {
            view.showShortToast(2, view.getActivity().getString(R.string.error_input_add_level));
            return;
        }

        view.showProgressBar(false, false, null, view.getActivity().getString(R.string.doing_update_setting));

        TransferObject transfer_money_to_point = new TransferObject();
        transfer_money_to_point.setValue(to_point);
        transfer_money_to_point.setFlag(1);

        ArrayList<TransferObject> money2Points = new ArrayList<>();
        money2Points.add(transfer_money_to_point);


        TransferObject transfer_money_from_point = new TransferObject();
        transfer_money_from_point.setValue(from_point);
        transfer_money_from_point.setFlag(1);

        ArrayList<TransferObject> point2Moneys = new ArrayList<>();
        point2Moneys.add(transfer_money_from_point);


        RESP_Setting resp_setting = new RESP_Setting();

        if (sortStore.getStore_type().equals(STORE_TYPE))
            resp_setting.setStore_id(sortStore.getId());
        else
            resp_setting.setChain_store_id(sortStore.getId());

        resp_setting.setMoney2Points(money2Points);
        resp_setting.setPoint2Moneys(point2Moneys);
        resp_setting.setLevels(arrayList);

        iCmd.execute(2, resp_setting);
    }


    public void setExists(boolean isExists) {
        this.isExists = isExists;
    }
}