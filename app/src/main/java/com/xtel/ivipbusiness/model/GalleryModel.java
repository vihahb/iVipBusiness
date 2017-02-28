package com.xtel.ivipbusiness.model;

import android.util.Log;

import com.xtel.ivipbusiness.model.entity.RESP_Picture;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.BasicModel;
import com.xtel.nipservicesdk.utils.JsonHelper;

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

    public void getListGallery(int store_id, int page, int pagesize, boolean isStore, ResponseHandle responseHandle) {
        String url = API_BASE;

        if (isStore)
            url += GET_LIST_GALLERY_STORE + store_id;
        else
            url += GET_LIST_GALLERY_CHAIN + store_id;

        url += GET_LIST_GALLERY_PAGE + page + GET_LIST_GALLERY_PAGESIZE + pagesize;
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

    public void addGallery(RESP_Picture resp_picture, boolean isStore, ResponseHandle responseHandle) {
        String url = API_BASE;

        if (isStore)
            url += ADD_GALLERY_STORE + resp_picture.getId();
        else
            url += ADD_GALLERY_CHAIN + resp_picture.getId();

        url += ADD_GALLERY_END;
        String session = LoginManager.getCurrentSession();

        Log.e("addGallery", "url " + url + "     session " + session);
        Log.e("addGallery", "jsonOpject " + JsonHelper.toJson(resp_picture));
        requestServer.postApi(url, JsonHelper.toJson(resp_picture), session, responseHandle);
    }
}
