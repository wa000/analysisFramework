package com.wa000.analysisFramework.analysis.provider.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils
{
    /**
     * 打印日志
     *
     * @param info
     */
    public static void log(String info)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()) + " - " + info);
    }


    /**
     * 打印耗时日志
     */
    public static void logCostTime(String info, long startTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long nowTime = System.currentTimeMillis();
        double time = (nowTime - startTime) / 1000d;
        System.out.println(sdf.format(new Date()) + " - " + info + "，耗时：" + time + "秒。");
    }
}
