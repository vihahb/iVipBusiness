package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Vulcl on 1/17/2017
 */

public class SortStore {
    @Expose
    private int id;
    @Expose
    private String banner;
    @Expose
    private String logo;
    @Expose
    private String name;
    @Expose
    private String store_type;
    @Expose
    private String address;
    @Expose
    private int bg_id;
    @Expose
    private long date_create;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore_type() {
        return store_type;
    }

    public void setStore_type(String store_type) {
        this.store_type = store_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBg_id() {
        return bg_id;
    }

    public void setBg_id(int bg_id) {
        this.bg_id = bg_id;
    }

    public long getDate_create() {
        return date_create;
    }

    public void setDate_create(long date_create) {
        this.date_create = date_create;
    }
}