package com.wa000.analysisFramework.analysis.provider.base.entity;

public class StockPlateData extends StockPlateDataKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_plate_data.rate
     *
     * @mbggenerated
     */
    private Float rate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_plate_data.cjCount
     *
     * @mbggenerated
     */
    private Long cjcount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_plate_data.cjPriceCount
     *
     * @mbggenerated
     */
    private Long cjpricecount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_plate_data.type
     *
     * @mbggenerated
     */
    private Integer type;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_plate_data.rate
     *
     * @return the value of stock_plate_data.rate
     *
     * @mbggenerated
     */
    public Float getRate() {
        return rate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_plate_data.rate
     *
     * @param rate the value for stock_plate_data.rate
     *
     * @mbggenerated
     */
    public void setRate(Float rate) {
        this.rate = rate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_plate_data.cjCount
     *
     * @return the value of stock_plate_data.cjCount
     *
     * @mbggenerated
     */
    public Long getCjcount() {
        return cjcount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_plate_data.cjCount
     *
     * @param cjcount the value for stock_plate_data.cjCount
     *
     * @mbggenerated
     */
    public void setCjcount(Long cjcount) {
        this.cjcount = cjcount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_plate_data.cjPriceCount
     *
     * @return the value of stock_plate_data.cjPriceCount
     *
     * @mbggenerated
     */
    public Long getCjpricecount() {
        return cjpricecount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_plate_data.cjPriceCount
     *
     * @param cjpricecount the value for stock_plate_data.cjPriceCount
     *
     * @mbggenerated
     */
    public void setCjpricecount(Long cjpricecount) {
        this.cjpricecount = cjpricecount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_plate_data.type
     *
     * @return the value of stock_plate_data.type
     *
     * @mbggenerated
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_plate_data.type
     *
     * @param type the value for stock_plate_data.type
     *
     * @mbggenerated
     */
    public void setType(Integer type) {
        this.type = type;
    }
}