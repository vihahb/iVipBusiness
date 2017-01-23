package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservice.model.entity.RESP_Basic;

/**
 * Created by Vulcl on 1/17/2017
 */

public class RESP_Full_Profile extends RESP_Basic {
    @Expose
    private String fullname;
    @Expose
    private int gender;
    @Expose
    private long birthday;
    @Expose
    private String email;
    @Expose
    private String phonenumber;
    @Expose
    private String address;
    @Expose
    private String avatar;
    @Expose
    private String qr_code;
    @Expose
    private String bar_code;
    @Expose
    private String status;
    @Expose
    private int store_number;
    @Expose
    private long join_date;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStore_number() {
        return store_number;
    }

    public void setStore_number(int store_number) {
        this.store_number = store_number;
    }

    public long getJoin_date() {
        return join_date;
    }

    public void setJoin_date(long join_date) {
        this.join_date = join_date;
    }
}