package com.xtel.nipservicesdk.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by vihahb on 1/4/2017.
 */

public class RegisterModel extends LoginNipModel {

    @Expose
    private String email;

    @Expose
    private int sendEmail;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(int sendEmail) {
        this.sendEmail = sendEmail;
    }

    @Override
    public String toString() {
        return "RegisterModel{" +
                "email='" + email + '\'' +
                ", sendEmail=" + sendEmail +
                '}';
    }
}
