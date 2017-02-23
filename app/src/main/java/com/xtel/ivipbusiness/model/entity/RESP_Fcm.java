package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Vulcl on 2/23/2017
 */

public class RESP_Fcm {
    @Expose
    private int news_id;
    @Expose
    private int notify_type;
    @Expose
    private long begin_time;
    @Expose
    private NotifyCodition notify_condition;

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public int getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(int notify_type) {
        this.notify_type = notify_type;
    }

    public long getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(long begin_time) {
        this.begin_time = begin_time;
    }

    public NotifyCodition getNotify_condition() {
        return notify_condition;
    }

    public void setNotify_condition(NotifyCodition notify_condition) {
        this.notify_condition = notify_condition;
    }
}
