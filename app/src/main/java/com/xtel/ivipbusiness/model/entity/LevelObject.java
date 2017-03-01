package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Vulcl on 3/1/2017.
 */

public class LevelObject {
    @Expose
    private Integer level_limit;
    @Expose
    private String level_name;
    @Expose
    private Integer level;

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
}