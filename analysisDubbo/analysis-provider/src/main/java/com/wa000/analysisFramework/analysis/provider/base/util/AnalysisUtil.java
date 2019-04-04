package com.wa000.analysisFramework.analysis.provider.base.util;

import com.wa000.analysisFramework.analysis.provider.base.dao.StockBaseMapper;
import com.wa000.analysisFramework.analysis.provider.base.entity.StockBase;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

/**
 * 分析工具类
 */
public class AnalysisUtil
{
    public static void main(String[] args)
    {
        autoAnalysisOneStock();
    }

    /**
     * 循环所有股票，自动生成特征，然后分析成功率，并存入数据库
     */
    public static void autoAnalysisOneStock()
    {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        long startTime = System.currentTimeMillis();

        // 获取所有股票信息
        StockBaseMapper stockBaseMapper = sqlSession.getMapper(StockBaseMapper.class);
        List<StockBase> stockBaseList = stockBaseMapper.selectByExample(null);
        if(null != stockBaseList)
        {
            for(StockBase oneStock : stockBaseList)
            {
                // 随机获取特征值
                // 特征值为 开盘价/收盘价/最高价/最低价/成交量/成交额 的变化率权重矩阵
                List<Double> characterList = new ArrayList<Double>();
                for(int i = 0; i < 6; i++)
                {
                    double character = (RandomUtils.nextInt(100) + 1) / 100d;
                    characterList.add(character);
                }

                String characterStr = "";
                for(Double one : characterList)
                {
                    characterStr = characterStr + one + "，";
                }
                characterStr = characterStr.substring(0, characterStr.length() - 1);
                LogUtils.log("开始分析股票 step 1 特征值为 : " + characterStr);
            }
        }
        LogUtils.logCostTime("", startTime);
    }



}
