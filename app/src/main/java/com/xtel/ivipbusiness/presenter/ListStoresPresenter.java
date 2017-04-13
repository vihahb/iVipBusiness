package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.RESP_List_Sort_Store;
import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.activity.inf.IListStoreView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/17/2017
 */

public class ListStoresPresenter {
    private IListStoreView view;

    private final String TYPE = "STORE_NOT_IN_CHAIN";
    private ArrayList<RESP_Store> arrayList;

    public ListStoresPresenter(IListStoreView view) {
        this.view = view;
    }

    private int PAGE = 1;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            if (params.length > 0) {
                int type = (int) params[0];
                if (type == 1) {
                    StoresModel.getInstance().getListStoreNotInChain(TYPE, PAGE, new ResponseHandle<RESP_List_Sort_Store>(RESP_List_Sort_Store.class) {
                        @Override
                        public void onSuccess(RESP_List_Sort_Store obj) {
                            PAGE++;
                            view.onGetListStoresSuccess(obj.getData());
                        }

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd);
                            else
                                view.onGetListStoresError(error);
                        }
                    });
                } else if (type == 2) {
                    StoresModel.getInstance().updateStore((RESP_Store) params[1], new ResponseHandle<RESP_None>(RESP_None.class) {
                        @Override
                        public void onSuccess(RESP_None obj) {
                            arrayList.remove((arrayList.size() - 1));
                            checkToUpdate();
                        }

                        @Override
                        public void onSuccess() {
                            arrayList.remove((arrayList.size() - 1));
                            checkToUpdate();
                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd);
                            else {
                                view.onChooseError();
                            }
                        }
                    });
                }
            }
        }
    };

    public void getListStores(boolean isClear) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        if (isClear)
            PAGE = 1;

        iCmd.execute(1);
    }

    public void chooseList(ArrayList<SortStore> listData) {
        if (arrayList == null)
            arrayList = new ArrayList<>();
        else
            arrayList.clear();

        for (int i = listData.size() - 1; i >= 0; i--) {
            if (listData.get(i).isCHecked()) {
                RESP_Store resp_store = new RESP_Store();


                /*
                * thiếu lat và lng
                * */

                resp_store.setId(listData.get(i).getId());
                resp_store.setChain_store_id(Constants.SORT_STORE.getId());
                resp_store.setStore_type(listData.get(i).getStore_type());

                arrayList.add(resp_store);
            }
        }

        if (arrayList.size() == 0) {
            view.showShortToast(view.getActivity().getString(R.string.error_no_store_selected));
            return;
        }

        checkToUpdate();
    }

    private void checkToUpdate() {
        if (arrayList.size() > 0) {
            iCmd.execute(2, arrayList.get((arrayList.size() - 1)));
        } else {
            view.onChooseSuccess();
        }
    }
}