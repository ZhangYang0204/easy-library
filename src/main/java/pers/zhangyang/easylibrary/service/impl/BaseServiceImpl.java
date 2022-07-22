package pers.zhangyang.easylibrary.service.impl;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import pers.zhangyang.easylibrary.base.DaoBase;
import pers.zhangyang.easylibrary.dao.VersionDao;
import pers.zhangyang.easylibrary.service.BaseService;
import pers.zhangyang.easylibrary.util.ResourceUtil;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BaseServiceImpl implements BaseService {

    @Override
    public void initDatabase(){
new VersionDao().init();
        InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream("easyLibrary.yml");
        YamlConfiguration yamlConfiguration=new YamlConfiguration();
        InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        try {
            yamlConfiguration.load(inputStreamReader);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        List<Class> classList = ResourceUtil.getClassesFromJarFile(yamlConfiguration.getStringList("daoPackage"));
        for (Class c : classList) {
            if (Modifier.isInterface(c.getModifiers()) || Modifier.isAbstract(c.getModifiers())) {
                continue;
            }
            if (!DaoBase.class.isAssignableFrom(c)) {
                continue;
            }
            DaoBase daoBase = null;
            try {
                daoBase = (DaoBase) c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            assert daoBase != null;
            daoBase.init();
        }

    }


}
