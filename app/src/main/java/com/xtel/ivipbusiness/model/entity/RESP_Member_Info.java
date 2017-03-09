package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

import java.io.Serializable;

/**
 * Created by Vulcl on 3/5/2017
 */

public class RESP_Member_Info extends RESP_Basic implements Serializable {
    @Expose
    private Integer store_id;
    @Expose
    private String user_code;
    @Expose
    private String full_name;
    @Expose
    private String avatar;
    @Expose
    private String member_card;
    @Expose
    private Long create_time;
    @Expose
    private String level_name;

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMember_card() {
        return member_card;
    }

    public void setMember_card(String member_card) {
        this.member_card = member_card;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }
}