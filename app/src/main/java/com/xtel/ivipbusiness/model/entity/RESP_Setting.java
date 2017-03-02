package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

import java.util.ArrayList;

/**
 * Created by Vulcl on 3/1/2017
 */

public class RESP_Setting extends RESP_Basic {
    @Expose
    private Integer store_id;
    @Expose
    private Integer chain_store_id;
    @Expose
    private Integer id;
    @Expose
    private ArrayList<TransferObject> money2Points;
    @Expose
    private ArrayList<TransferObject> point2Moneys;
    @Expose
    private ArrayList<LevelObject> levels;

    public Integer getStore_id() {
        return store_id;
    }

    public void setStore_id(Integer store_id) {
        this.store_id = store_id;
    }

    public Integer getChain_store_id() {
        return chain_store_id;
    }

    public void setChain_store_id(Integer chain_store_id) {
        this.chain_store_id = chain_store_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<TransferObject> getMoney2Points() {
        return money2Points;
    }

    public void setMoney2Points(ArrayList<TransferObject> money2Points) {
        this.money2Points = money2Points;
    }

    public ArrayList<TransferObject> getPoint2Moneys() {
        return point2Moneys;
    }

    public void setPoint2Moneys(ArrayList<TransferObject> point2Moneys) {
        this.point2Moneys = point2Moneys;
    }

    public ArrayList<LevelObject> getLevels() {
        return levels;
    }

    public void setLevels(ArrayList<LevelObject> levels) {
        this.levels = levels;
    }
}