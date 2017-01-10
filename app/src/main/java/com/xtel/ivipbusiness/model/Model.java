package com.xtel.ivipbusiness.model;

/**
 * Created by Lê Công Long Vũ on 12/28/2016
 */

public class Model extends BasicModel {
    private static Model instance = new Model();

    public static Model getInstance() {
        return instance;
    }
}
