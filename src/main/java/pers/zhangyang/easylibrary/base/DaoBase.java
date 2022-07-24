package pers.zhangyang.easylibrary.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 所有Dao的父类，继承该类的Dao类会在插件开启的时候自动调用init方法，会在执行重载命令的时候自动调用init方法，并且还要在easyLibrary.yml指定的包下
 */
public abstract class DaoBase {
    /**
     * 里面填写创建数据库表的语句
     * @return sql语句执行成功影响的条数
     */
    public abstract int init();

    /**
     * 一个连接池，存放了每个线程对应的Connection实例
     */
    private static final ThreadLocal<Connection> t = new ThreadLocal<>();

    /**
     * 获得当前线程的Connection实例，每个线程使用此方法获得的实例不相同，用此方法得到的实例无需释放任何资源，任由gc回收
     * 当线程的实例被关闭了则自动创建一个新的
     * @return
     */
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


    /**
     * 自动将ResultSet的第一条记录转化
     * @param rs 查询到的ResultSet
     * @param cls 需要转化为什么类的class对象
     * @param <T> 转化的结果
     * @return
     */
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

                    if (f.getType().isAssignableFrom(boolean.class)) {
                        f.set(obj, rs.getBoolean(ReplaceUtil.replaceToDatabaseTableName(f.getName())));
                    }else {
                        f.set(obj, rs.getObject(ReplaceUtil.replaceToDatabaseTableName(f.getName())));
                    }

                }
                return obj;
            }
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 自动将ResultSet转化为List
     * @param rs 查询到的ResultSet
     * @param cls 需要转化为什么类的class对象
     * @param <T> 转化的结果
     * @return
     */
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
                    if (f.getType().isAssignableFrom(boolean.class)) {
                        f.set(obj, rs.getBoolean(ReplaceUtil.replaceToDatabaseTableName(f.getName())));
                    }else {
                        f.set(obj, rs.getObject(ReplaceUtil.replaceToDatabaseTableName(f.getName())));
                    }
                }
                list.add(obj);//添加到集合
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
        return list;
    }
}
