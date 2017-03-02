package com.xtel.ivipbusiness.model;

import android.util.Log;

import com.xtel.ivipbusiness.model.entity.RESP_List_Sort_Store;
import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.BasicModel;
import com.xtel.nipservicesdk.utils.JsonHelper;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/17/2017
 */

public class StoresModel extends BasicModel {
    private static StoresModel instance;

    public static StoresModel getInstance() {
        if (instance == null)
            instance = new StoresModel();
        return instance;
    }

    public void addStore(String jsonObject, ResponseHandle responseHandle) {
        String url = API_BASE + ADD_STORE;
        String session = LoginManager.getCurrentSession();
        requestServer.postApi(url, jsonObject, session, responseHandle);
    }

    public void updateStore(RESP_Store resp_store, ResponseHandle responseHandle) {
        String url = API_BASE + UPDATE_STORE;
        String session = LoginManager.getCurrentSession();

        Log.e("updateStore", "url " + url + "    session " + session);
        Log.e("updateStore", "RESP_Store " + JsonHelper.toJson(resp_store));
        requestServer.putApi(url, JsonHelper.toJson(resp_store), session, responseHandle);
    }

    public void getStoreInfo(int store_id, String store_type, ResponseHandle responseHandle) {
        String url = API_BASE + GET_STORE_INFO_ID + store_id + GET_STORE_INFO_TYPE + store_type;
        String session = LoginManager.getCurrentSession();

        Log.e("getStoreInfo", "url " + url + "    session " + session);
        requestServer.postApi(url, null, session, responseHandle);
    }

    public void getListChains(String type, int page, int pagesize, ResponseHandle responseHandle) {
        String url = API_BASE + GET_LIST_CHAIN_TYPE + type + GET_LIST_CHAIN_PAGE + page + GET_LIST_CHAIN_PAGE_SIZE + pagesize;
        String session = LoginManager.getCurrentSession();

        Log.e("GET_LIST_CHAIN", "url " + url + "     session " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void getListStoreInChain(int store_id, int page, int pagesize, ResponseHandle responseHandle) {
        String url = API_BASE + GET_LIST_STORE_IN_CHAIN + store_id + GET_LIST_STORE_IN_CHAIN_PAGE + page + GET_LIST_STORE_IN_CHAIN_PAGESIZE + pagesize;
        String session = LoginManager.getCurrentSession();

        Log.e("GET_LIST_CHAIN", "url " + url + "     session " + session);
        requestServer.postApi(url, null, session, responseHandle);
    }

    public void getListStoreNotInChain(ResponseHandle responseHandle) {
        RESP_List_Sort_Store resp_list_sort_store = new RESP_List_Sort_Store();

        ArrayList<SortStore> arrayList = new ArrayList<>();
        for (int i = 1; i <= 21; i++) {
            SortStore stores = new SortStore();
            stores.setId(i);
            stores.setBanner("http://cuonghungthinh.com/imagesup/banner%20coffee.png");
            stores.setLogo("https://www.merryallcenter.org/wp-content/uploads/2016/04/cup-150x150.png");
            stores.setName("Store " + i);
            stores.setStore_type("STYPE");
            stores.setAddress("Toa nha Phuong Nga, ngo 84 Tran Thai Tong, Ha Noi");
            stores.setBg_id(0);
            stores.setDate_create(1456765200000L);

            arrayList.add(stores);
        }

        resp_list_sort_store.setData(arrayList);
        responseHandle.onSuccess(JsonHelper.toJson(resp_list_sort_store));
    }

    public void getStoreSetting(int id, String type, ResponseHandle responseHandle) {
        String url = API_BASE + GET_SETTING_TYPE + type + GET_SETTING_ID + id;
        String session = LoginManager.getCurrentSession();

        Log.e("getStoreSetting", "url " + url + "     session " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void addStoreSetting(String jsonObject, ResponseHandle responseHandle) {
        String url = API_BASE + ADD_SETTING;
        String session = LoginManager.getCurrentSession();

        Log.e("addStoreSetting", "url " + url + "     session " + session);
        Log.e("addStoreSetting", jsonObject);
        requestServer.postApi(url, jsonObject, session, responseHandle);
    }

    public void getMemberCheckIn(int store_id, boolean isStore, int page, int pagesize, ResponseHandle responseHandle) {
        String url = API_BASE;

        if (isStore)
            url += GET_MEMBER_STORE_IN + store_id;
        else
            url += GET_MEMBER_CHAIN_IN + store_id;

        url += GET_MEMBER_PAGE + page + GET_MEMBER_PAGESIZE + pagesize;
        String session = LoginManager.getCurrentSession();

        Log.e("getMemberCheckIn", "url " + url + "     session " + session);
        requestServer.getApi(url, session, responseHandle);
    }
}