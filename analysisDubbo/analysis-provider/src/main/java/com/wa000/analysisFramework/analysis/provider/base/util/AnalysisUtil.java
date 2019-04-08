package com.wa000.analysisFramework.analysis.provider.base.util;

import com.wa000.analysisFramework.analysis.provider.base.dao.StockAnalysisLogMapper;
import com.wa000.analysisFramework.analysis.provider.base.dao.StockAnalysisMapper;
import com.wa000.analysisFramework.analysis.provider.base.dao.StockBaseMapper;
import com.wa000.analysisFramework.analysis.provider.base.dao.StockPriceHisMapper;
import com.wa000.analysisFramework.analysis.provider.base.entity.*;
import org.apache.commons.lang.StringUtils;
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
        while(true)
        {
            try
            {
                autoAnalysisOneStock();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 循环所有股票，自动生成特征，然后分析成功率，并存入数据库
     */
    public static void autoAnalysisOneStock()
    {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        long startTime = System.currentTimeMillis();

        // 获取所有股票信息
        LogUtils.log("开始获取股票列表");
        StockBaseMapper stockBaseMapper = sqlSession.getMapper(StockBaseMapper.class);
        List<StockBase> stockBaseList = stockBaseMapper.selectByExample(null);
        LogUtils.logCostTime("获取成功", startTime);
        LogUtils.log("开始获取分析记录");
        startTime = System.currentTimeMillis();
        StockAnalysisLogMapper stockAnalysisLogMapper = sqlSession.getMapper(StockAnalysisLogMapper.class);
        StockAnalysisLogExample analysisLogExample = new StockAnalysisLogExample();
        analysisLogExample.setOrderByClause("count");
        List<StockAnalysisLog> stockAnalysisLogList = stockAnalysisLogMapper.selectByExample(analysisLogExample);
        LogUtils.logCostTime("获取成功", startTime);

        // 校验如果两边数据不相等就添加缺少的数据
        if(null != stockAnalysisLogList && null != stockBaseList)
        {
            if(stockAnalysisLogList.size() != stockBaseList.size())
            {
                // TODO 增加补全数据逻辑
            }
        }

        if(null != stockAnalysisLogList)
        {
            for(StockAnalysisLog oneLog : stockAnalysisLogList)
            {
                if(null != stockBaseList)
                {
                    for(StockBase oneStock : stockBaseList)
                    {
                        if(oneStock.getId().equals(oneLog.getId()))
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
                            LogUtils.log("开始分析股票" + oneStock.getId() + " step 1 特征值为 : " + characterStr);

                            // 获取股票历史数据
                            LogUtils.log("开始获取" + oneLog.getId() + "历史数据");
                            startTime = System.currentTimeMillis();
                            StockPriceHisMapper stockPriceHisMapper = sqlSession.getMapper(StockPriceHisMapper.class);
                            StockPriceHisExample stockPriceHisExample = new StockPriceHisExample();
                            stockPriceHisExample.createCriteria().andIdEqualTo(oneLog.getId());
                            stockPriceHisExample.setOrderByClause("logdate desc");
                            List<StockPriceHis> stockPriceHisList = stockPriceHisMapper.selectByExample(stockPriceHisExample);
                            LogUtils.logCostTime("获取成功", startTime);

                            // 根据特征值计算成功率
                            int count = 0;
                            for(int i = 0; i < stockPriceHisList.size() - 2; i++)
                            {
                                StockPriceHis stockPriceHis0 = stockPriceHisList.get(i);
                                StockPriceHis stockPriceHis1 = stockPriceHisList.get(i + 1);
                                StockPriceHis stockPriceHis2 = stockPriceHisList.get(i + 2);

                                float f1 = stockPriceHis1.getBeginprice() - stockPriceHis2.getBeginprice();
                                float f2 = stockPriceHis1.getEndprice() - stockPriceHis2.getEndprice();
                                float f3 = stockPriceHis1.getTopprice() - stockPriceHis2.getTopprice();
                                float f4 = stockPriceHis1.getFootprice() - stockPriceHis2.getFootprice();
                                float f5 = (stockPriceHis1.getCjpricecount() - stockPriceHis2.getCjpricecount()) * (stockPriceHis1.getEndprice() / stockPriceHis1.getCjpricecount());
                                float f6 = (stockPriceHis1.getCount() - stockPriceHis2.getCount()) * (stockPriceHis1.getEndprice() / stockPriceHis1.getCount());

                                double all = f1 * characterList.get(0) + f2 * characterList.get(1) + f3 * characterList.get(2) + f4 * characterList.get(3) + f5 * characterList.get(4) + f6 * characterList.get(5);
                                if(all > 0)
                                {
                                    if(stockPriceHis0.getRate() > 0)
                                    {
                                        count++;
                                    }
                                }
                                else if(all < 0)
                                {
                                    if(stockPriceHis0.getRate() < 0)
                                    {
                                        count++;
                                    }
                                }
                            }

                            LogUtils.log("step 2 开始分析成功率并插入数据");
                            double rate = ((double) count) / (stockPriceHisList.size() - 2);
                            StockAnalysis analysis = new StockAnalysis();
                            analysis.setId(oneLog.getId());
                            analysis.setCharactermark(characterStr.hashCode() + "");
                            analysis.setCharacterlog(characterStr);
                            analysis.setSuccessrate(rate);
                            analysis.setAnalysiscount(stockPriceHisList.size() - 2);
                            StockAnalysisMapper stockAnalysisMapper = sqlSession.getMapper(StockAnalysisMapper.class);
                            stockAnalysisMapper.insert(analysis);
                            sqlSession.commit();

                            StockAnalysisLogExample analysisLogExample1 = new StockAnalysisLogExample();
                            analysisLogExample1.createCriteria().andIdEqualTo(oneLog.getId());
                            List<StockAnalysisLog> result = stockAnalysisLogMapper.selectByExample(analysisLogExample1);
                            if(null != result && !result.isEmpty())
                            {
                                StockAnalysisLog stockAnalysisLog = result.get(0);
                                stockAnalysisLog.setCount(stockAnalysisLog.getCount() + 1);
                                if(StringUtils.isNotEmpty(stockAnalysisLog.getCharacterrate()))
                                {
                                    if(Double.parseDouble(stockAnalysisLog.getCharacterrate()) < rate)
                                    {
                                        stockAnalysisLog.setCharacterlog(characterStr);
                                        stockAnalysisLog.setCharacterrate(rate + "");
                                    }
                                }
                                else
                                {
                                    stockAnalysisLog.setCharacterlog(characterStr);
                                    stockAnalysisLog.setCharacterrate(rate + "");
                                }

                                stockAnalysisLogMapper.updateByPrimaryKey(stockAnalysisLog);
                                sqlSession.commit();
                            }

                            break;
                        }
                    }
                    LogUtils.log("step 3 " + oneLog.getId() + "分析完成");
                    LogUtils.logCostTime("", startTime);
                }
            }
        }
    }



}
