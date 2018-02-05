import com.alibaba.fastjson.JSON;
import com.alphalion.crawl.GalaxyCrawlApp;
import com.alphalion.crawl.mapper.ProductSymbolsNetEntityMapper;
import com.alphalion.crawl.mapper.entity.ProductSymbolsNetEntity;
import com.alphalion.crawl.service.ICrawlService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author SongBaoYu
 * @date 2018/1/10 下午2:54
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GalaxyCrawlApp.class)
@TestPropertySource("classpath:application-test2.properties")
public class CrawlUtilTests {

    @Autowired
    private ICrawlService crawlService;

    @Autowired
    private ProductSymbolsNetEntityMapper productSymbolsNetEntityMapper;

    @Test
    public void testCrawlIdentifier() {
    }


    @Test
    public void testRepeatProductSymbolsnet() {
        HashSet<ProductSymbolsNetEntity> set = new HashSet<>();
        ProductSymbolsNetEntity net = new ProductSymbolsNetEntity();
        net.setSymbol("ssss");
        net.setCusip("cccc");
        net.setIsin("iiii");
        set.add(net);

        net = new ProductSymbolsNetEntity();
        net.setSymbol("ssss");
        net.setCusip("cccc");
        net.setIsin("iiii");
        set.add(net);


        net = new ProductSymbolsNetEntity();
        net.setSymbol("s");
        net.setCusip("c");
        net.setIsin("i");
        set.add(net);

        System.out.println("set====" + JSON.toJSON(set));

    }


    @Test
    public void testPDF() {
        try {
            crawlService.crawlSymbolsFromPDF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void insertNet() {
        List<ProductSymbolsNetEntity> list = Arrays.asList(new ProductSymbolsNetEntity("466367109", "US4663671091", "JACK", "")
                , new ProductSymbolsNetEntity("466367109", "US4663671091", "JACK", ""));

        int rows = productSymbolsNetEntityMapper.batchInsertSymbolsIgnoreErrors(list);
        System.out.println("rows==="+rows);
    }


}
