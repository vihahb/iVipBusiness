package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Vulcl on 1/19/2017
 */

public class Member implements Serializable {
    @Expose
    private String fullname;
    @Expose
    private String avatar;
    @Expose
    private int level;
    @Expose
    private String level_name;
    @Expose
    private int total_point;
    @Expose
    private int current_point;
    @Expose
    private int checkin_number;
    @Expose
    private double total_money;
    @Expose
    private Long last_checkin;
    @Expose
    private Long buy_number;
    @Expose
    private String store_address;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public int getTotal_point() {
        return total_point;
    }

    public void setTotal_point(int total_point) {
        this.total_point = total_point;
    }

    public int getCurrent_point() {
        return current_point;
    }

    public void setCurrent_point(int current_point) {
        this.current_point = current_point;
    }

    public int getCheckin_number() {
        return checkin_number;
    }

    public void setCheckin_number(int checkin_number) {
        this.checkin_number = checkin_number;
    }

    public double getTotal_money() {
        return total_money;
    }

    public void setTotal_money(double total_money) {
        this.total_money = total_money;
    }

    public Long getLast_checkin() {
        return last_checkin;
    }

    public void setLast_checkin(Long last_checkin) {
        this.last_checkin = last_checkin;
    }

    public Long getBuy_number() {
        return buy_number;
    }

    public void setBuy_number(Long buy_number) {
        this.buy_number = buy_number;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }
}