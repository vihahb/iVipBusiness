package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

import java.io.Serializable;

/**
 * Created by Vulcl on 2/20/2017
 */

public class RESP_News extends RESP_Basic implements Serializable {
    @Expose
    private int id;
    @Expose
    private Integer store_id;
    @Expose
    private Integer chain_store_id;
    @Expose
    private int news_type;
    @Expose
    private String banner;
    @Expose
    private String description;
    @Expose
    private String title;
    @Expose
    private Voucher voucher;
    @Expose
    private boolean is_public;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getChain_store_id() {
        return chain_store_id;
    }

    public void setChain_store_id(int chain_store_id) {
        this.chain_store_id = chain_store_id;
    }

    public int getNews_type() {
        return news_type;
    }

    public void setNews_type(int news_type) {
        this.news_type = news_type;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public boolean is_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }
}
