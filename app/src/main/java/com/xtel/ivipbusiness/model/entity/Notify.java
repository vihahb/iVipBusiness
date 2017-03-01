package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Vulcl on 2/28/2017.
 */

public class Notify {
    @Expose
    private int id;
    @Expose
    private Long create_time;
    @Expose
    private int notify_type;
    @Expose
    private Long begin_time;
    @Expose
    private NotifyCodition notify_condition;
    @Expose
    private int news_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public int getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(int notify_type) {
        this.notify_type = notify_type;
    }

    public Long getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(Long begin_time) {
        this.begin_time = begin_time;
    }

    public NotifyCodition getNotify_condition() {
        return notify_condition;
    }

    public void setNotify_condition(NotifyCodition notify_condition) {
        this.notify_condition = notify_condition;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }
}