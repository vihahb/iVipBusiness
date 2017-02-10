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

    public void getStoreInfo(ResponseHandle responseHandle) {
        RESP_Store resp_store = new RESP_Store();

        resp_store.setId(1);
        resp_store.setBanner("http://cuonghungthinh.com/imagesup/banner%20coffee.png");
        resp_store.setLogo("https://www.merryallcenter.org/wp-content/uploads/2016/04/cup-150x150.png");
        resp_store.setName("Le Cong Long Vu");
        resp_store.setStore_type("Kim cuong");
        resp_store.setCreate_time(1480611600000L);
        resp_store.setNews_number(1);
        resp_store.setAddress("Toa nha Phuong Nga, ngo 84 Tran Thai Tong, Ha Noi");
        resp_store.setLocation_lat(21.029668);
        resp_store.setLocation_lng(105.804094);
        resp_store.setPhonenumber("0986495949");
        resp_store.setDescription("Thanh lap nam 1999, trai qua 15 nam hinh thanh – xay dung – phat trien, Khoi Giao duc FPT da tro thanh he thong giao duc lon cua Viet Nam, gom cac he giao duc dao tao THPT, Cao dang, Dai hoc, sau Dai hoc, dao tao nghe, dao tao danh cho khoi doanh nghiep… va cac du an uom tao. Hien Khoi da dat quy mo gan 1.000 can bo, giang vien va gan 15.000 hoc sinh, sinh vien, hoc vien o tat ca cac he dao tao.");
        resp_store.setQr_code("https://www.shopify.com/growth-tools-assets/qr-code/shopify-faae7065b7b351d28495b345ed76096c03de28bac346deb1e85db632862fd0e4.png");
        resp_store.setBar_code("http://i.fonts2u.com/ba/bar-code-39_4.png");

        responseHandle.onSuccess(JsonHelper.toJson(resp_store));
    }

    public void getListChains(String type, int page, int pagesize, ResponseHandle responseHandle) {
        String url = API_BASE + GET_LIST_CHAIN_TYPE + type + GET_LIST_CHAIN_PAGE + page + GET_LIST_CHAIN_PAGE_SIZE + pagesize;
        String session = LoginManager.getCurrentSession();

        Log.e("GET_LIST_CHAIN", "url " + url);
        Log.e("GET_LIST_CHAIN", "session " + session);
        requestServer.getApi(url, session, responseHandle);


//        RESP_List_Sort_Store resp_list_sort_store = new RESP_List_Sort_Store();
//
//        ArrayList<SortStore> arrayList = new ArrayList<>();
//        for (int i = 1; i <= 21; i++) {
//            SortStore stores = new SortStore();
//            stores.setId(i);
//            stores.setBanner("http://cuonghungthinh.com/imagesup/banner%20coffee.png");
//            stores.setLogo("https://www.merryallcenter.org/wp-content/uploads/2016/04/cup-150x150.png");
//            stores.setName("Store " + i);
//            stores.setStore_type("STYPE");
//            stores.setAddress("Toa nha Phuong Nga, ngo 84 Tran Thai Tong, Ha Noi");
//            stores.setBg_id(0);
//            stores.setDate_create(1456765200000L);
//
//            arrayList.add(stores);
//        }
//
//        resp_list_sort_store.setData(arrayList);
//        responseHandle.onSuccess(JsonHelper.toJson(resp_list_sort_store));
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
}