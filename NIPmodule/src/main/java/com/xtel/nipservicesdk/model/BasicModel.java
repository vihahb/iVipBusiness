package com.xtel.nipservicesdk.model;

import com.xtel.nipservicesdk.callback.RequestServer;

/**
 * Created by Lê Công Long Vũ on 1/3/2017
 */

public abstract class BasicModel {
    protected final String API_BASE = "http://124.158.5.112:9190/ivip-b";

    //    Usser
    protected final String GET_FULL_INFO_USER = "/v0.1/user?type=Full";
    protected final String GET_SHORT_INFO_USER = "/v0.1/user?type=Sort";
    protected final String UPDATE_USER = "/v0.1/user";

//    Store
    protected final String GET_LIST_CHAIN_TYPE = "/v0.1/store?type=";
    protected final String GET_LIST_CHAIN_PAGE = "&page=";
    protected final String GET_LIST_CHAIN_PAGE_SIZE = "&pagesize=";

    protected final String GET_LIST_STORE_IN_CHAIN = "/v0.1/chain/";
    protected final String GET_LIST_STORE_IN_CHAIN_PAGE = "/stores?page=";
    protected final String GET_LIST_STORE_IN_CHAIN_PAGESIZE = "&pagesize=";

    protected final String GET_LIST_GALLERY = "/v0.1/store/";
    protected final String GET_LIST_GALLERY_PAGE = "/gallery?page=";
    protected final String GET_LIST_GALLERY_PAGESIZE = "&pagesize=";

    protected final String DELETE_GALLERY_STORE_ID = "/v0.1/store/";
    protected final String DELETE_GALLERY_ID = "/gallery/";

    protected final String ADD_STORE = "/v0.1/store/";
    protected final String UPDATE_STORE = "/v0.1/store/";

    protected final String GET_STORE_INFO_ID = "/v0.1/store/";
    protected final String GET_STORE_INFO_TYPE = "?type=";

//    Bản tin
    protected final String GET_NEWS_PAGE = "/v0.1/news?page=";
    protected final String GET_NEWS_PAGESISE = "&pagesize=";
    protected final String GET_NEWS_ID = "&id=";
    protected final String GET_NEWS_TYPE = "&type=";

    protected final String ADD_NEWS = "/v0.1/news";
    protected final String GET_NEWS = "/v0.1/news/";
    protected final String UPDATE_NEWS = "/v0.1/news";

//    Fcm
    protected final String SEND_NOTIFY = "/v0.1/news/notify";

    //    Google
    protected final String GET_ADDRESS_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    protected final String GET_ADDRESS_KEY = "&key=";

    //    User
    protected final String USER_FULLNAME = "user_fullname";
    protected final String USER_GENDER = "user_gender";
    protected final String USER_BIRTHDAY = "user_birthday";
    protected final String USER_EMAIL = "user_email";
    protected final String USER_PHONENUMBER = "user_phonenumber";
    protected final String USER_ADDRESS = "user_address";
    protected final String USER_AVATAR = "user_avatar";
    protected final String USER_QR_CODE = "user_qr_code";
    protected final String USER_BAR_CODE = "user_bar_code";
    protected final String USER_STATUS = "user_status";
    protected final String USER_STORE_NUMBER = "user_store_number";
    protected final String USER_JOIN_DATE = "user_join_date";

    protected RequestServer requestServer = new RequestServer();
}
