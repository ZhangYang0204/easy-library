package pers.zhangyang.easylibrary.service.impl;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.base.DaoBase;
import pers.zhangyang.easylibrary.base.YamlBase;
import pers.zhangyang.easylibrary.dao.VersionDao;
import pers.zhangyang.easylibrary.meta.VersionMeta;
import pers.zhangyang.easylibrary.service.BaseService;
import pers.zhangyang.easylibrary.util.ResourceUtil;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class BaseServiceImpl implements BaseService {

    public static final BaseServiceImpl INSTANCE = new BaseServiceImpl();

    @Override
    public void initDatabase() throws SQLException, IOException, InvalidConfigurationException {

        InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream("easyLibrary.yml");
        YamlConfiguration yamlConfiguration=new YamlConfiguration();
        InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        yamlConfiguration.load(inputStreamReader);
        List<Class> classList = ResourceUtil.getClasssFromJarFile(yamlConfiguration.getStringList("daoPackage"));
        for (Class c : classList) {
            if (Modifier.isInterface(c.getModifiers()) || Modifier.isAbstract(c.getModifiers())) {
                continue;
            }
            if (!DaoBase.class.isAssignableFrom(c)) {
                continue;
            }
            DaoBase daoBase = null;
            try {
                daoBase = (DaoBase) c.getField("INSTANCE").get(c);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            assert daoBase != null;
            daoBase.init();
        }

    }


}
