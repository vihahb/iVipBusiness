package com.xtel.ivipbusiness.model;

import com.xtel.ivipbusiness.model.entity.RESP_List_Sort_Store;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.utils.JsonHelper;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/17/2017
 */

public class StoresModel {
    private static StoresModel instance;

    public static StoresModel getInstance() {
        if (instance == null)
            instance = new StoresModel();
        return instance;
    }

    public void getListChains(ResponseHandle responseHandle) {
        RESP_List_Sort_Store resp_list_sort_store = new RESP_List_Sort_Store();

        ArrayList<SortStore> arrayList = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            SortStore stores = new SortStore();
            stores.setId(i);
            stores.setBanner("http://3.bp.blogspot.com/-fGedPqEK7VA/USrzhrrrPAI/AAAAAAAAGrw/aywfic1mJYY/s1600/mobile_game_banner_jp.naver.lineplay.android.jpeg");
            stores.setLogo("https://www.merryallcenter.org/wp-content/uploads/2016/04/cup-150x150.png");
            stores.setName("Store " + i);
            stores.setStore_type("STYPE");
            stores.setAddress("Toa nha Phuong Nga, ngo 84 Tran Thai Tong, Ha Noi");
            stores.setBg_id(0);

            arrayList.add(stores);
        }

        resp_list_sort_store.setData(arrayList);
        responseHandle.onSuccess(JsonHelper.toJson(resp_list_sort_store));
    }

    public void getListStoreNotInChain(ResponseHandle responseHandle) {
        RESP_List_Sort_Store resp_list_sort_store = new RESP_List_Sort_Store();

        ArrayList<SortStore> arrayList = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            SortStore stores = new SortStore();
            stores.setId(i);
            stores.setBanner("http://3.bp.blogspot.com/-fGedPqEK7VA/USrzhrrrPAI/AAAAAAAAGrw/aywfic1mJYY/s1600/mobile_game_banner_jp.naver.lineplay.android.jpeg");
            stores.setLogo("https://www.merryallcenter.org/wp-content/uploads/2016/04/cup-150x150.png");
            stores.setName("Store " + i);
            stores.setStore_type("STYPE");
            stores.setAddress("Toa nha Phuong Nga, ngo 84 Tran Thai Tong, Ha Noi");
            stores.setBg_id(0);

            arrayList.add(stores);
        }

        resp_list_sort_store.setData(arrayList);
        responseHandle.onSuccess(JsonHelper.toJson(resp_list_sort_store));
    }
}