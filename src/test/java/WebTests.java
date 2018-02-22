import com.alibaba.fastjson.JSON;
import com.alphalion.crawl.GalaxyCrawlApp;
import com.alphalion.crawl.controller.TestController;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author SongBaoYu
 * @date 2018/2/12 下午4:12
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GalaxyCrawlApp.class)
public class WebTests {


    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testHello() {

        Result result = restTemplate.getForObject("/hello/dddd", Result.class);
        System.out.println("res==="+ JSON.toJSONString(result));
    }


}
