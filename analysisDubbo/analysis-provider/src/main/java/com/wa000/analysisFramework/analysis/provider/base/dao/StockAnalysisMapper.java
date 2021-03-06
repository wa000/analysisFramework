package com.wa000.analysisFramework.analysis.provider.base.dao;

import com.wa000.analysisFramework.analysis.provider.base.entity.StockAnalysis;
import com.wa000.analysisFramework.analysis.provider.base.entity.StockAnalysisExample;
import com.wa000.analysisFramework.analysis.provider.base.entity.StockAnalysisKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockAnalysisMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    int countByExample(StockAnalysisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    int deleteByExample(StockAnalysisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(StockAnalysisKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    int insert(StockAnalysis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    int insertSelective(StockAnalysis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    List<StockAnalysis> selectByExampleWithBLOBs(StockAnalysisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    List<StockAnalysis> selectByExample(StockAnalysisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    StockAnalysis selectByPrimaryKey(StockAnalysisKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") StockAnalysis record, @Param("example") StockAnalysisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") StockAnalysis record, @Param("example") StockAnalysisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") StockAnalysis record, @Param("example") StockAnalysisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(StockAnalysis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(StockAnalysis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_analysis
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(StockAnalysis record);
}