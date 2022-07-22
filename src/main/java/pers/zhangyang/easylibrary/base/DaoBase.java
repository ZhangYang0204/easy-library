package pers.zhangyang.easylibrary.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class DaoBase {
    public abstract int init();

    private static final ThreadLocal<Connection> t = new ThreadLocal<>();

    public static Connection getConnection(){
        Connection connection = t.get();
        try {
            if (connection == null || connection.isClosed()) {
                DatabaseYaml databaseYamlManager = DatabaseYaml.INSTANCE;
                connection = DriverManager.getConnection(
                        databaseYamlManager.getStringDefault("database.url"),
                        databaseYamlManager.getString("database.username"),
                        databaseYamlManager.getString("database.password"));
                connection.setAutoCommit(false);
                t.set(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }


    //将查询结果的第一条数据转换为指定类型的对象
    @Nullable
    public static <T> T singleTransform(ResultSet rs, Class<T> cls) {
        try {
            //只会将查询的第一天数据转换为对象
            if(rs.next()){
                //实例化对象
                T obj=cls.newInstance();
                //获取类中所有的属性
                Field[] arrf=cls.getDeclaredFields();
                //遍历属性
                for(Field f:arrf){
                    //设置忽略访问校验
                    f.setAccessible(true);
                    //为属性设置内容
                    f.set(obj, rs.getObject(f.getName()));
                }
                return obj;
            }
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
        return null;
    }
    //将查询结果所有数据转换为指定类型的对象
    @NotNull
    public static <T> List<T> multipleTransform(ResultSet rs, Class<T> cls) {
        List<T> list=new ArrayList<>();
        try {
            //将查询的所有数据转换为对象添加到集合
            while(rs.next()){
                //实例化对象
                T obj=cls.newInstance();
                //获取类中所有的属性
                Field[] arrf=cls.getDeclaredFields();
                //遍历属性
                for(Field f:arrf){
                    //设置忽略访问校验
                    f.setAccessible(true);
                    //为属性设置内容
                    f.set(obj, rs.getObject(f.getName()));
                }
                list.add(obj);//添加到集合
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
}
