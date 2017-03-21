package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

/**
 * Created by Vulcl on 3/20/2017
 */

public class RESP_Valid_Check_News extends RESP_Basic {
    @Expose
    private Long create_time;
    @Expose
    private Long expired_time;
    @Expose
    private Long used_time;
    @Expose
    private String customer_name;
    @Expose
    private String title;
    @Expose
    private String banner;
    @Expose
    private String news_id;

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Long getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(Long expired_time) {
        this.expired_time = expired_time;
    }

    public Long getUsed_time() {
        return used_time;
    }

    public void setUsed_time(Long used_time) {
        this.used_time = used_time;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }
}