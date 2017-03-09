package com.xtel.ivipbusiness.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.Area;
import com.xtel.ivipbusiness.model.entity.LevelObject;
import com.xtel.ivipbusiness.model.entity.NotifyCodition;
import com.xtel.ivipbusiness.model.entity.RESP_Setting;
import com.xtel.ivipbusiness.view.activity.inf.ISendFcmView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.commons.Constants;

import java.util.ArrayList;

/**
 * Created by Vulcl on 2/22/2017
 */

public class SendFcmPresenter {
    private ISendFcmView view;

    private int news_type = -1;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            StoresModel.getInstance().getStoreSetting(Constants.SORT_STORE.getId(), Constants.SORT_STORE.getStore_type(), new ResponseHandle<RESP_Setting>(RESP_Setting.class) {
                @Override
                public void onSuccess(final RESP_Setting obj) {
                    if (obj.getLevels().size() == 0)
                        view.onRequestError(new Error(501, "ERROR", view.getActivity().getString(R.string.error_try_again)));
                    else
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.onGetSettingSuccess(obj.getLevels());
                            }
                        }, 5000);
                }

                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Error error) {
                    if (error.getCode() == 2)
                        view.getNewSession(iCmd);
                    else
                        view.onRequestError(error);
                }
            });
        }
    };

    public SendFcmPresenter(ISendFcmView view) {
        this.view = view;
    }

    public void getData() {
        try {
            news_type = view.getActivity().getIntent().getIntExtra(Constants.MODEL, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (news_type == -1 || Constants.SORT_STORE == null)
            view.onGetDataError();
        else {
            view.onGetDataSuccess(news_type);
            if (news_type == 2)
                iCmd.execute();
        }
    }

    public void doneOption(ArrayList<Area> arrayList_area, ArrayList<LevelObject> arrayList_level, int gender, int from_age, int to_age) {
        Integer[] area = checkArea(arrayList_area);
        Integer[] level = checkLevel(arrayList_level);

        if (!checkData(area, gender, level, from_age, to_age))
            return;

        NotifyCodition notifyCodition = new NotifyCodition();
        notifyCodition.setAreas(area);
        notifyCodition.setGender(gender);
        notifyCodition.setLevel(level);
        notifyCodition.setFrom_age(from_age);
        notifyCodition.setTo_age(to_age);

        Log.e("NotifyCodition", JsonHelper.toJson(notifyCodition));

        Intent intent = new Intent();
        intent.putExtra(Constants.MODEL, notifyCodition);
        view.getActivity().setResult(Activity.RESULT_OK, intent);
        view.getActivity().finish();
    }

    private boolean checkData(Integer[] area, int gender, Integer[] level, int from_age, int to_age) {
        if (area.length == 0) {
            view.showShortToast(-1, view.getActivity().getString(R.string.error_input_area));
            return false;
        }
        if (gender == 0) {
            view.showShortToast(-1, view.getActivity().getString(R.string.error_input_gender));
            return false;
        }

        if (news_type == 2)
            if (level.length == 0) {
                view.showShortToast(-1, view.getActivity().getString(R.string.error_input_level));
                return false;
            }

        if (from_age == -1) {
            view.showShortToast(1, view.getActivity().getString(R.string.error_input_age));
            return false;
        }
        if (to_age == -1) {
            view.showShortToast(2, view.getActivity().getString(R.string.error_input_age));
            return false;
        }

        return true;
    }

    private Integer[] checkArea(ArrayList<Area> arrayList_area) {
        int total = 0;

        for (int i = arrayList_area.size() - 1; i >= 0; i--) {
            if (arrayList_area.get(i).isSelected())
                total++;
        }

        Integer[] area = new Integer[total];

        if (area.length == 0)
            return area;

        total = 0;
        for (int i = 0; i < arrayList_area.size(); i++) {
            if (arrayList_area.get(i).isSelected()) {
                area[total] = (i + 1);
                total++;
            }
        }

        return area;
    }

    private Integer[] checkLevel(ArrayList<LevelObject> arrayList_level) {
        int total = 0;

        for (int i = arrayList_level.size() - 1; i >= 0; i--) {
            if (arrayList_level.get(i).isSelected())
                total++;
        }

        Integer[] level = new Integer[total];

        if (level.length == 0)
            return level;

        total = 0;
        for (int i = 0; i < arrayList_level.size(); i++) {
            if (arrayList_level.get(i).isSelected()) {
                level[total] = (i + 1);
                total++;
            }
        }

        return level;
    }
}