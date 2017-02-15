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

//    Business
    protected final String GET_LIST_CHAIN_TYPE = "/v0.1/store?type=";
    protected final String GET_LIST_CHAIN_PAGE = "&page=";
    protected final String GET_LIST_CHAIN_PAGE_SIZE = "&pagesize=";

    protected final String ADD_STORE = "/v0.1/store/";
    protected final String UPDATE_STORE = "/v0.1/store/";

    protected final String GET_STORE_INFO_ID = "/v0.1/store/";
    protected final String GET_STORE_INFO_TYPE = "?type=";

    //    Google
    protected final String GET_ADDRESS_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    protected final String GET_ADDRESS_KEY = "&key=";

    protected RequestServer requestServer = new RequestServer();
}
