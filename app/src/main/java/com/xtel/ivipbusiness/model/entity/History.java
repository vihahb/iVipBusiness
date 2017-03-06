package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Vulcl on 1/19/2017
 */

public class History {
    @Expose
    private Long action_time;
    @Expose
    private Integer action_type;
    @Expose
    private String action_name;
    @Expose
    private String action_desc;
    @Expose
    private Long money;
    @Expose
    private Long point;

    public Long getAction_time() {
        return action_time;
    }

    public void setAction_time(Long action_time) {
        this.action_time = action_time;
    }

    public Integer getAction_type() {
        return action_type;
    }

    public void setAction_type(Integer action_type) {
        this.action_type = action_type;
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public String getAction_desc() {
        return action_desc;
    }

    public void setAction_desc(String action_desc) {
        this.action_desc = action_desc;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }
}