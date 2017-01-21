package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Vulcl on 1/19/2017
 */

public class Member implements Serializable {
    @Expose
    private int id;
    @Expose
    private String fullname;
    @Expose
    private String avatar;
    @Expose
    private int total_point;
    @Expose
    private int remaining_point;
    @Expose
    private int total_checkin;
    @Expose
    private int total_shopping;
    @Expose
    private int total_shopping_in_store;
    @Expose
    private long last_checkin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getTotal_point() {
        return total_point;
    }

    public void setTotal_point(int total_point) {
        this.total_point = total_point;
    }

    public int getRemaining_point() {
        return remaining_point;
    }

    public void setRemaining_point(int remaining_point) {
        this.remaining_point = remaining_point;
    }

    public int getTotal_checkin() {
        return total_checkin;
    }

    public void setTotal_checkin(int total_checkin) {
        this.total_checkin = total_checkin;
    }

    public int getTotal_shopping() {
        return total_shopping;
    }

    public void setTotal_shopping(int total_shopping) {
        this.total_shopping = total_shopping;
    }

    public int getTotal_shopping_in_store() {
        return total_shopping_in_store;
    }

    public void setTotal_shopping_in_store(int total_shopping_in_store) {
        this.total_shopping_in_store = total_shopping_in_store;
    }

    public long getLast_checkin() {
        return last_checkin;
    }

    public void setLast_checkin(long last_checkin) {
        this.last_checkin = last_checkin;
    }
}