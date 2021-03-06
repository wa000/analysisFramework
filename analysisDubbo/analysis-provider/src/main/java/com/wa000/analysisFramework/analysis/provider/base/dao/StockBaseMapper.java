package com.wa000.analysisFramework.analysis.provider.base.dao;

import com.wa000.analysisFramework.analysis.provider.base.entity.StockBase;
import com.wa000.analysisFramework.analysis.provider.base.entity.StockBaseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockBaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_base
     *
     * @mbggenerated
     */
    int countByExample(StockBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_base
     *
     * @mbggenerated
     */
    int deleteByExample(StockBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_base
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_base
     *
     * @mbggenerated
     */
    int insert(StockBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_base
     *
     * @mbggenerated
     */
    int insertSelective(StockBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_base
     *
     * @mbggenerated
     */
    List<StockBase> selectByExample(StockBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_base
     *
     * @mbggenerated
     */
    StockBase selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_base
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") StockBase record, @Param("example") StockBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_base
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") StockBase record, @Param("example") StockBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_base
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(StockBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_base
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(StockBase record);
}