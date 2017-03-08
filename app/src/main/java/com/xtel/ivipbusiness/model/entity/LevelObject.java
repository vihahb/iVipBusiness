package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Vulcl on 3/1/2017.
 */

public class LevelObject implements Serializable {
    @Expose
    private Integer level_limit;
    @Expose
    private String level_name;
    @Expose
    private Integer level;
    @Expose
    private String member_card;
    @Expose
    private String url_card;

    public Integer getLevel_limit() {
        return level_limit;
    }

    public void setLevel_limit(Integer level_limit) {
        this.level_limit = level_limit;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getMember_card() {
        return member_card;
    }

    public void setMember_card(String member_card) {
        this.member_card = member_card;
    }

    public String getUrl_card() {
        return url_card;
    }

    public void setUrl_card(String url_card) {
        this.url_card = url_card;
    }
}