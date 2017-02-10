package com.xtel.nipservicesdk.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Lê Công Long Vũ on 1/6/2017
 */

public class ActiveNip {
    @Expose
    private String authorization_code;
    @Expose
    private String activation_code;
    @Expose
    private String service_code;
    @Expose
    private String accountType;

    public String getAuthorization_code() {
        return authorization_code;
    }

    public void setAuthorization_code(String authorization_code) {
        this.authorization_code = authorization_code;
    }

    public String getActivation_code() {
        return activation_code;
    }

    public void setActivation_code(String activation_code) {
        this.activation_code = activation_code;
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

    @Override
    public String toString() {
        return "ActiveNip{" +
                "authorization_code='" + authorization_code + '\'' +
                ", activation_code='" + activation_code + '\'' +
                ", service_code='" + service_code + '\'' +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}