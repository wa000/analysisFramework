package com.wa000.analysisFramework.analysis.provider.base.entity;

import java.util.Date;

public class StockPriceHisBackKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_price_his_back.id
     *
     * @mbggenerated
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_price_his_back.logDate
     *
     * @mbggenerated
     */
    private Date logdate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_price_his_back.id
     *
     * @return the value of stock_price_his_back.id
     *
     * @mbggenerated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_price_his_back.id
     *
     * @param id the value for stock_price_his_back.id
     *
     * @mbggenerated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_price_his_back.logDate
     *
     * @return the value of stock_price_his_back.logDate
     *
     * @mbggenerated
     */
    public Date getLogdate() {
        return logdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_price_his_back.logDate
     *
     * @param logdate the value for stock_price_his_back.logDate
     *
     * @mbggenerated
     */
    public void setLogdate(Date logdate) {
        this.logdate = logdate;
    }
}