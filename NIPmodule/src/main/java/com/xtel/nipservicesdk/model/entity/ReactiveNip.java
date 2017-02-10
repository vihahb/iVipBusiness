package com.xtel.nipservicesdk.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by vihahb on 1/5/2017.
 */

public class ReactiveNip {
    @Expose
    private String username;
    @Expose
    private String service_code;
    @Expose
    private int sendMail;
    @Expose
    private String accountType;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public int getSendMail() {
        return sendMail;
    }

    public void setSendMail(int sendMail) {
        this.sendMail = sendMail;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "ReactiveNip{" +
                "username='" + username + '\'' +
                ", service_code='" + service_code + '\'' +
                ", sendMail=" + sendMail +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}
