package com.xtel.nipservicesdk.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by vihahb on 1/5/2017.
 */

public class ResetEntity {

    @Expose
    private String email;

    @Expose
    private String usename;


    @Expose
    private String service_code;

    @Expose
    private int sendMail;

    @Expose
    private String accountType;

    @Expose
    private String password;

    @Expose
    private String authorization_code;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthorization_code() {
        return authorization_code;
    }

    public void setAuthorization_code(String authorization_code) {
        this.authorization_code = authorization_code;
    }

    @Override
    public String toString() {
        return "ResetEntity{" +
                "email='" + email + '\'' +
                ", usename='" + usename + '\'' +
                ", service_code='" + service_code + '\'' +
                ", sendMail=" + sendMail +
                ", accountType='" + accountType + '\'' +
                ", password='" + password + '\'' +
                ", authorization_code='" + authorization_code + '\'' +
                '}';
    }
}
