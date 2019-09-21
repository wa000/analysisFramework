import com.wa000.analysisFramework.analysis.provider.base.dao.StockBaseMapper;
import com.wa000.analysisFramework.analysis.provider.base.dao.StockPriceHisMapper;
import com.wa000.analysisFramework.analysis.provider.base.entity.StockBase;
import com.wa000.analysisFramework.analysis.provider.base.entity.StockBaseExample;
import com.wa000.analysisFramework.analysis.provider.base.entity.StockPriceHis;
import com.wa000.analysisFramework.analysis.provider.base.entity.StockPriceHisExample;
import com.wa000.analysisFramework.analysis.provider.base.util.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 测试类
 */
public class teatMain {

    public static void main(String[] args) throws Exception
    {
        testDB();
    }

    /**
     * 测试数据库连接
     *
     * @throws ParseException
     */
    private static void testDB() throws ParseException {
        long oldTime = System.currentTimeMillis();
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StockBaseMapper mapper = sqlSession.getMapper(StockBaseMapper.class);

        StockBaseExample example = new StockBaseExample();
        StockBaseExample.Criteria criteria = example.createCriteria();
        criteria.andLtpanBetween(0l, 100000000l);

        List<StockBase> stockBaseList = mapper.selectByExample(example);

        StockPriceHisMapper mapper1 = sqlSession.getMapper(StockPriceHisMapper.class);
        StockPriceHisExample example1 = new StockPriceHisExample();
        StockPriceHisExample.Criteria criteria1 = example1.createCriteria();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        criteria1.andLogdateEqualTo(simpleDateFormat.parse("2019-04-28 16:00:01"));
        List<StockPriceHis> stockPriceHis = mapper1.selectByExample(example1);

        long currentTime = System.currentTimeMillis();

        for(StockBase oneBean : stockBaseList)
        {
            System.out.println(oneBean.getId() + "," + oneBean.getStockname() + "," + oneBean.getLtpan());
        }
        System.out.println("耗时：" + ((currentTime - oldTime) / 1000d) + "秒");
        System.out.println(stockBaseList.size());
        System.out.println(stockPriceHis.size());
    }
}
