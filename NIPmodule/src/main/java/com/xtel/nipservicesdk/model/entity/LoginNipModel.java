package com.xtel.nipservicesdk.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by vihahb on 1/4/2017.
 */

public class LoginNipModel {
    @Expose
    private String username;

    @Expose
    private String password;

    @Expose
    private String service_code;

    @Expose
    private String accountType;

    @Expose
    private DeviceObject devInfo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public DeviceObject getDevInfo() {
        return devInfo;
    }

    public void setDevInfo(DeviceObject devInfo) {
        this.devInfo = devInfo;
    }

    @Override
    public String toString() {
        return "LoginNipModel{" +
                "devInfo=" + devInfo +
                ", accountType='" + accountType + '\'' +
                ", service_code='" + service_code + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
