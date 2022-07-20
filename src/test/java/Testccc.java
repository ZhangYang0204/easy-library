import org.junit.Test;
import pers.zhangyang.easylibrary.executor.ReloadPluginExecutor;

public class Testccc {

    @Test
    public void cc(){
        String name=ReloadPluginExecutor.class.getName();
        name=name.substring(0,name.length()-8);
        name=name.split("\\.")[name.split("\\.").length-1];
    }
}
