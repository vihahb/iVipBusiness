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
    protected final String GET_LIST_CHAIN_PAGE_SIZE = "&pagesize=10";

    protected final String GET_LIST_STORE_IN_CHAIN = "/v0.1/chain/";
    protected final String GET_LIST_STORE_IN_CHAIN_PAGE = "/stores?page=";
    protected final String GET_LIST_STORE_IN_CHAIN_PAGESIZE = "&pagesize=";

    protected final String ADD_STORE = "/v0.1/store/";
    protected final String UPDATE_STORE = "/v0.1/store/";

    protected final String GET_STORE_INFO_ID = "/v0.1/store/";
    protected final String GET_STORE_INFO_TYPE = "?type=";

    protected final String ADD_SETTING = "/v0.1/setting";
    protected final String GET_SETTING_TYPE = "/v0.1/setting?type=";
    protected final String GET_SETTING_ID = "&id=";

    protected final String GET_MEMBER_CHAIN_IN = "/v0.1/chain/";
    protected final String GET_MEMBER_STORE_IN = "/v0.1/store/";
    protected final String GET_MEMBER_PAGE = "/checkins?page=";
    protected final String GET_MEMBER_PAGESIZE = "&pagesize=";

    //    Notify
    protected final String SEND_NOTIFY = "/v0.1/news/notify";
    protected final String GET_LIST_NOTIFY = "/v0.1/news/notify/";

    protected final String REGISTER_FCM = "/v0.1/user/fcm";
    protected final String FCM_CLOUD_KEY = "fcm_cloud_key";

    //    Gallery
    protected final String GET_LIST_GALLERY_STORE = "/v0.1/store/";
    protected final String GET_LIST_GALLERY_CHAIN = "/v0.1/chain/";
    protected final String GET_LIST_GALLERY_PAGE = "/gallery?page=";
    protected final String GET_LIST_GALLERY_PAGESIZE = "&pagesize=";

    protected final String DELETE_GALLERY_STORE_ID = "/v0.1/store/";
    protected final String DELETE_GALLERY_ID = "/gallery/";

    protected final String ADD_GALLERY_CHAIN = "/v0.1/chain/";
    protected final String ADD_GALLERY_STORE = "/v0.1/store/";
    protected final String ADD_GALLERY_END = "/gallery";

    //    Statistic
    protected final String GET_STATISTIC_STORE_ID = "/v0.1/report?store_id=";
    protected final String GET_STATISTIC_CHAIN_ID = "/v0.1/report?chain_id=";
    protected final String GET_STATISTIC_TYPE = "&type=";
    protected final String GET_STATISTIC_ACTION = "&action=";
    protected final String GET_STATISTIC_DAY = "&day=";

    //    News
    protected final String GET_NEWS_PAGE = "/v0.1/news?page=";
    protected final String GET_NEWS_PAGESISE = "&pagesize=";
    protected final String GET_NEWS_ID = "&id=";
    protected final String GET_NEWS_TYPE = "&type=";

    protected final String ADD_NEWS = "/v0.1/news";
    protected final String GET_NEWS = "/v0.1/news/";
    protected final String UPDATE_NEWS = "/v0.1/news";

    protected final String VALID_CHECK_VOUCHER = "/v0.1/voucher/";
    protected final String USE_VOUCHER = "/v0.1/voucher/used";

    protected final String VOUCHER_CODE = "voucher_code";
    protected final String LAT = "lat";
    protected final String LNG = "lng";

    //    Member
    protected final String GET_MEMBER_INFO_ID = "/v0.1/store/";
    protected final String GET_MEMBER_INFO_MEMBER_CARD = "/member_card/";

    protected final String SAVE_GET_MEMBER_CARD_TEMPLATE = "/v0.1/store/member_card_template";
    protected final String CREATE_MEMBER_CARD = "/v0.1/store/member_card";
    protected final String SAVE_POINT = "/v0.1/store/save_point";

    protected final String GET_MEMBER_HISTORY_STORE = "/v0.1/store/";
    protected final String GET_MEMBER_HISTORY_MEMBER_CODE = "/history/";
    protected final String GET_MEMBER_HISTORY_MEMBER_PAGE = "?page=";
    protected final String GET_MEMBER_HISTORY_MEMBER_PAGESIZE = "&pagesize=";

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