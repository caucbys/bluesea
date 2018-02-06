import com.alibaba.fastjson.JSON;
import com.alphalion.crawl.GalaxyCrawlApp;
import com.alphalion.crawl.controller.dto.ArgItem;
import com.alphalion.crawl.controller.dto.ClassDto;
import com.alphalion.crawl.controller.dto.Result;
import com.alphalion.crawl.mapper.ProductSymbolsNetEntityMapper;
import com.alphalion.crawl.mapper.entity.ProductSymbolsNetEntity;
import com.alphalion.crawl.service.ICrawlService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
        System.out.println("rows===" + rows);
    }

    @Test
    public void reflect() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ClassDto dto = new ClassDto();
        dto.setClazz(Result.class);
        dto.setMethod("listSuccessed");


        dto.setArgs(new ArgItem[]{new ArgItem(Object.class, Arrays.asList("1", "2", "3")), new ArgItem(Long.class, 10L)});


        Method method = null;
        ArgItem[] args = dto.getArgs();
        if (null != args) {
            Class<?>[] paramTypes = new Class[args.length];
            for (int i = 0; i < args.length; ++i) {
                paramTypes[i] = args[i].getType();
            }
            method = dto.getClazz().getMethod(dto.getMethod(), paramTypes);
        }


        if (null != method) {
            Object instance = dto.getClazz().newInstance();
            Object invoke = method.invoke(instance, args[0].getValue(), args[1].getValue());
            System.out.println("res=====" + JSON.toJSON(invoke));
        }


    }


}
