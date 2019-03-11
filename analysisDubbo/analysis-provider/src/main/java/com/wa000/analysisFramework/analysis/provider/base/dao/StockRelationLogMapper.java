package com.wa000.analysisFramework.analysis.provider.base.dao;

import com.wa000.analysisFramework.analysis.provider.base.entity.StockRelationLog;
import com.wa000.analysisFramework.analysis.provider.base.entity.StockRelationLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockRelationLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_relation_log
     *
     * @mbggenerated
     */
    int countByExample(StockRelationLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_relation_log
     *
     * @mbggenerated
     */
    int deleteByExample(StockRelationLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_relation_log
     *
     * @mbggenerated
     */
    int insert(StockRelationLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_relation_log
     *
     * @mbggenerated
     */
    int insertSelective(StockRelationLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_relation_log
     *
     * @mbggenerated
     */
    List<StockRelationLog> selectByExample(StockRelationLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_relation_log
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") StockRelationLog record, @Param("example") StockRelationLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_relation_log
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") StockRelationLog record, @Param("example") StockRelationLogExample example);
}