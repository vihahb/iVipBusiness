package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Vulcl on 2/23/2017
 */

public class NotifyCodition implements Serializable {
    @Expose
    private Integer[] level;
    @Expose
    private Integer gender;
    @Expose
    private Integer from_age;
    @Expose
    private Integer to_age;
    @Expose
    private Integer[] areas;

    public Integer[] getLevel() {
        return level;
    }

    public void setLevel(Integer[] level) {
        this.level = level;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getFrom_age() {
        return from_age;
    }

    public void setFrom_age(Integer from_age) {
        this.from_age = from_age;
    }

    public Integer getTo_age() {
        return to_age;
    }

    public void setTo_age(Integer to_age) {
        this.to_age = to_age;
    }

    public Integer[] getAreas() {
        return areas;
    }

    public void setAreas(Integer[] areas) {
        this.areas = areas;
    }
}