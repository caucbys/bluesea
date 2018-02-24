import com.alphalion.crawl.GalaxyCrawlApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author SongBaoYu
 * @date 2018/2/12 下午4:12
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GalaxyCrawlApp.class)
public class WebTests {


    @Test
    public void testClass() {
        System.out.println("the first instance:");
        TestClass.getInstance();
        System.out.println("");
        System.out.println("the second instance:");
        TestClass.getInstance();
    }


}
