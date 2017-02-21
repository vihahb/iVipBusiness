package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Vulcl on 2/20/2017
 */

public class Voucher {
    @Expose
    private Long begin_time;
    @Expose
    private Long finish_time;
    @Expose
    private Long time_alive;
    @Expose
    private int number_of_voucher;
    @Expose
    private Double sales;
    @Expose
    private int sales_type;
    @Expose
    private int point;

    public Long getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(Long begin_time) {
        this.begin_time = begin_time;
    }

    public Long getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(Long finish_time) {
        this.finish_time = finish_time;
    }

    public Long getTime_alive() {
        return time_alive;
    }

    public void setTime_alive(Long time_alive) {
        this.time_alive = time_alive;
    }

    public int getNumber_of_voucher() {
        return number_of_voucher;
    }

    public void setNumber_of_voucher(int number_of_voucher) {
        this.number_of_voucher = number_of_voucher;
    }

    public Double getSales() {
        return sales;
    }

    public void setSales(Double sales) {
        this.sales = sales;
    }

    public int getSales_type() {
        return sales_type;
    }

    public void setSales_type(int sales_type) {
        this.sales_type = sales_type;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}