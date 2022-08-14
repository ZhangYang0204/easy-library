import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class test {
    @Test
    public void on(){
        Map m = new HashMap<>();
        m.put("inventoryType","类型");
        m.put("inventorySize", "大小");
        m.put(1,null);
        Gson gson=new GsonBuilder().serializeNulls().create();
        String jason=gson.toJson(m);

        System.out.println(jason);
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        m=gson.fromJson(jason,mapType);

        System.out.println(m.get(1));
    }
}
