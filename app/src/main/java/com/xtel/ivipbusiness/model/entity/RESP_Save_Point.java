package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Vulcl on 3/5/2017
 */

public class RESP_Save_Point {
    @Expose
    private int store_id;
    @Expose
    private String user_code;
    @Expose
    private int money;
    @Expose
    private String bill_code;
    @Expose
    private String bill_url;

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getBill_code() {
        return bill_code;
    }

    public void setBill_code(String bill_code) {
        this.bill_code = bill_code;
    }

    public String getBill_url() {
        return bill_url;
    }

    public void setBill_url(String bill_url) {
        this.bill_url = bill_url;
    }
}