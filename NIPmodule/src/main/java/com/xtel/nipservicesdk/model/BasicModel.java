package com.xtel.nipservicesdk.model;

import com.xtel.nipservicesdk.callback.RequestServer;

/**
 * Created by Lê Công Long Vũ on 1/3/2017
 */

public abstract class BasicModel {
    protected String API_BASE = "http://124.158.5.112:9190/ivip-b";

//    Usser
    protected String GET_FULL_INFO_USER = "/v0.1//user?type={Full}";
    protected String GET_SHORT_INFO_USER = "/v0.1//user?type={Sort}";

//    Business
    protected String GET_LIST_CHAIN_TYPE = "/v0.1/store?type=";
    protected String GET_LIST_CHAIN_PAGE = "&page=";
    protected String GET_LIST_CHAIN_PAGE_SIZE = "&pagesize=";

    protected String ADD_STORE = "/v0.1/store/";

    protected String GET_STORE_INFO_ID = "/v0.1/store/";
    protected String GET_STORE_INFO_TYPE = "?type=";

    //    Google
    public static final String GET_ADDRESS_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    public static final String GET_ADDRESS_KEY = "&key=";

    protected RequestServer requestServer = new RequestServer();
}
