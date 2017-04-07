package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.StatisticModel;
import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.RESP_List_Sort_Store;
import com.xtel.ivipbusiness.model.entity.RESP_Statistic;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.fragment.inf.IStatisticView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Vulcl on 3/10/2017
 */

public class StatisticPresenter {
    protected IStatisticView view;

    private final String TYPE = "ALL";
    private int PAGE = 1;

    protected ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = (int) params[0];

                switch (type) {
                    case 1:
                        StoresModel.getInstance().getListChains(TYPE, PAGE, new ResponseHandle<RESP_List_Sort_Store>(RESP_List_Sort_Store.class) {
                            @Override
                            public void onSuccess(RESP_List_Sort_Store obj) {
                                PAGE++;
                                view.onGetStoresSuccess(obj.getData());
                            }

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Error error) {
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onRequestError(error);
                            }
                        });
                        break;
                    case 2:
                        StatisticModel.getInstance().getStatistic((SortStore) params[1], (int) params[2], (int) params[3], new ResponseHandle<RESP_Statistic>(RESP_Statistic.class) {
                            @Override
                            public void onSuccess(RESP_Statistic obj) {
                                view.onGetStatistic((int) params[2], obj.getData());
                            }

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Error error) {
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onRequestError(error);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public StatisticPresenter(IStatisticView view) {
        this.view = view;
    }

    public void getListStore() {
        if (!NetWorkInfo.isOnline(view.getActivity()))
            view.onNoInternet();
        else
            iCmd.execute(1);
    }

    public void getStatistic(SortStore sortStore, int day) {
        if (!NetWorkInfo.isOnline(view.getActivity()))
            view.onNoInternet();
        else {
            iCmd.execute(2, sortStore, 1, day);
            iCmd.execute(2, sortStore, 2, day);
            iCmd.execute(2, sortStore, 3, day);
        }
    }
}