package com.wa000.analysisFramework.analysis.provider.base.entity;

public class StockRelation {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_relation.stockId
     *
     * @mbggenerated
     */
    private String stockid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock_relation.relation
     *
     * @mbggenerated
     */
    private String relation;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_relation.stockId
     *
     * @return the value of stock_relation.stockId
     *
     * @mbggenerated
     */
    public String getStockid() {
        return stockid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_relation.stockId
     *
     * @param stockid the value for stock_relation.stockId
     *
     * @mbggenerated
     */
    public void setStockid(String stockid) {
        this.stockid = stockid == null ? null : stockid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock_relation.relation
     *
     * @return the value of stock_relation.relation
     *
     * @mbggenerated
     */
    public String getRelation() {
        return relation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock_relation.relation
     *
     * @param relation the value for stock_relation.relation
     *
     * @mbggenerated
     */
    public void setRelation(String relation) {
        this.relation = relation == null ? null : relation.trim();
    }
}