/**
 * @author SongBaoYu
 * @date 2018/2/23 上午10:35
 */
public class TestClass {

    private static class Holder{
        private static TestClass TESTCLASS=new TestClass();

        public Holder() {
            System.out.println("Holder construct start...");
        }
    }

    private TestClass() {
        System.out.println("TestClass construct start...");
    }


    public static final TestClass getInstance(){
        return Holder.TESTCLASS;
    }


    static {
        System.out.println("TestClass static block start...");
    }





}
