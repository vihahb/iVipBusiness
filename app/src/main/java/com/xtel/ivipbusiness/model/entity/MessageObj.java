package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Vulcl on 3/23/2017
 */

public class MessageObj implements Serializable {
    @Expose
    private String title;
    @Expose
    private String body;
    @Expose
    private Integer action;
    @Expose
    private String content;

    public MessageObj() {
    }

    public MessageObj(String title, String body, Integer action, String content) {
        this.title = title;
        this.body = body;
        this.action = action;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}