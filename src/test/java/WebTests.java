import com.alphalion.crawl.GalaxyCrawlApp;
import com.alphalion.crawl.service.ICacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author SongBaoYu
 * @date 2018/2/12 下午4:12
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GalaxyCrawlApp.class)
public class WebTests {


    @Autowired
    private ICacheService cacheService;

    @Test
    public void testCache() {
        cacheService.updateCacheByKey(ICacheService.BUSINESS_DATE_CACHE_KEY, "2018-04-04 00:00:00");
        Date date = cacheService.getBusinessDate();
        System.out.println("data===" + date);
    }


}
