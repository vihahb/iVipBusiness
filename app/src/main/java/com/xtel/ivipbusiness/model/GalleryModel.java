package com.xtel.ivipbusiness.model;

import android.util.Log;

import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.BasicModel;

/**
 * Created by Vulcl on 2/27/2017
 */

public class GalleryModel extends BasicModel {
    private static GalleryModel instance;

    public static GalleryModel getInstance() {
        if (instance == null)
            instance = new GalleryModel();
        return instance;
    }

    public void getListGallery(int store_id, int page, int pagesize, ResponseHandle responseHandle) {
        String url = API_BASE + GET_LIST_GALLERY + store_id + GET_LIST_GALLERY_PAGE + page + GET_LIST_GALLERY_PAGESIZE + pagesize;
        String session = LoginManager.getCurrentSession();

        Log.e("getListGallery", "url " + url + "     session " + session);
        requestServer.postApi(url, null, session, responseHandle);
    }

    public void deleteGallery(int store_id, int gallery_id, ResponseHandle responseHandle) {
        String url = API_BASE + DELETE_GALLERY_STORE_ID + store_id + DELETE_GALLERY_ID + gallery_id;
        String session = LoginManager.getCurrentSession();

        Log.e("deleteGallery", "url " + url + "     session " + session);
        requestServer.deleteApi(url, "", session, responseHandle);
    }
}
