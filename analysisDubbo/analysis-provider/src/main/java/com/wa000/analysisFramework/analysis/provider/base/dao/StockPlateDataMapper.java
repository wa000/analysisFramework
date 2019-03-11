package com.wa000.analysisFramework.analysis.provider.base.dao;

import com.wa000.analysisFramework.analysis.provider.base.entity.StockPlateData;
import com.wa000.analysisFramework.analysis.provider.base.entity.StockPlateDataExample;
import com.wa000.analysisFramework.analysis.provider.base.entity.StockPlateDataKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockPlateDataMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_plate_data
     *
     * @mbggenerated
     */
    int countByExample(StockPlateDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_plate_data
     *
     * @mbggenerated
     */
    int deleteByExample(StockPlateDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_plate_data
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(StockPlateDataKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_plate_data
     *
     * @mbggenerated
     */
    int insert(StockPlateData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_plate_data
     *
     * @mbggenerated
     */
    int insertSelective(StockPlateData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_plate_data
     *
     * @mbggenerated
     */
    List<StockPlateData> selectByExample(StockPlateDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_plate_data
     *
     * @mbggenerated
     */
    StockPlateData selectByPrimaryKey(StockPlateDataKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_plate_data
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") StockPlateData record, @Param("example") StockPlateDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_plate_data
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") StockPlateData record, @Param("example") StockPlateDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_plate_data
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(StockPlateData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_plate_data
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(StockPlateData record);
}