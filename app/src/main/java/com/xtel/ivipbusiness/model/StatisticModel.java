package com.xtel.ivipbusiness.model;

import android.util.Log;

import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.BasicModel;

/**
 * Created by Vulcl on 3/10/2017
 */

public class StatisticModel extends BasicModel {
    private static StatisticModel instance;

    public static StatisticModel getInstance() {
        if (instance == null)
            instance = new StatisticModel();
        return instance;
    }

    public void getStatistic(SortStore sortStore, int action, int day, ResponseHandle responseHandle) {
        String url = API_BASE;
            if (sortStore.getStore_type().equals("STORE"))
                url += GET_STATISTIC_STORE_ID + sortStore.getId();
            else
                url += GET_STATISTIC_CHAIN_ID + sortStore.getId();
        url += GET_STATISTIC_TYPE + sortStore.getStore_type() + GET_STATISTIC_ACTION + action + GET_STATISTIC_DAY + day;
        String session = LoginManager.getCurrentSession();

        Log.e("getStatistic", "url      " + url + "          session   " + session);
        requestServer.getApi(url, session, responseHandle);
    }
}