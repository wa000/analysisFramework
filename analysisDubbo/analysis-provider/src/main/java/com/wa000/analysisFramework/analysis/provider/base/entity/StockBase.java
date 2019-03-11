package com.wa000.analysisFramework.analysis.provider.base.entity;

import java.util.Date;

public class StockBase {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_base.id
     *
     * @mbggenerated
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_base.stockName
     *
     * @mbggenerated
     */
    private String stockname;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_base.type
     *
     * @mbggenerated
     */
    private String type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_base.ltPan
     *
     * @mbggenerated
     */
    private Long ltpan;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_base.updateTime
     *
     * @mbggenerated
     */
    private Date updatetime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_base.id
     *
     * @return the value of stock_base.id
     *
     * @mbggenerated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_base.id
     *
     * @param id the value for stock_base.id
     *
     * @mbggenerated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_base.stockName
     *
     * @return the value of stock_base.stockName
     *
     * @mbggenerated
     */
    public String getStockname() {
        return stockname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_base.stockName
     *
     * @param stockname the value for stock_base.stockName
     *
     * @mbggenerated
     */
    public void setStockname(String stockname) {
        this.stockname = stockname == null ? null : stockname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_base.type
     *
     * @return the value of stock_base.type
     *
     * @mbggenerated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_base.type
     *
     * @param type the value for stock_base.type
     *
     * @mbggenerated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_base.ltPan
     *
     * @return the value of stock_base.ltPan
     *
     * @mbggenerated
     */
    public Long getLtpan() {
        return ltpan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_base.ltPan
     *
     * @param ltpan the value for stock_base.ltPan
     *
     * @mbggenerated
     */
    public void setLtpan(Long ltpan) {
        this.ltpan = ltpan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_base.updateTime
     *
     * @return the value of stock_base.updateTime
     *
     * @mbggenerated
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_base.updateTime
     *
     * @param updatetime the value for stock_base.updateTime
     *
     * @mbggenerated
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}