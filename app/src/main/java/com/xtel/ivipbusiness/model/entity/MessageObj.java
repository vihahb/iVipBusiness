package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Vulcl on 3/23/2017
 */

public class MessageObj implements Serializable {
    @Expose
    private Integer action;
    @Expose
    private String content;

    public MessageObj() {
    }

    public MessageObj(Integer action, String content) {
        this.action = action;
        this.content = content;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}