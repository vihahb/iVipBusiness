package com.xtel.nipservicesdk.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Lê Công Long Vũ on 1/5/2017
 */

public class AuthenNip {
    @Expose
    private String authenticationid;
    @Expose
    private String service_code;
    @Expose
    private DeviceObject devInfo;

    public String getAuthenticationid() {
        return authenticationid;
    }

    public void setAuthenticationid(String authenticationid) {
        this.authenticationid = authenticationid;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public DeviceObject getDevInfo() {
        return devInfo;
    }

    public void setDevInfo(DeviceObject devInfo) {
        this.devInfo = devInfo;
    }
}
