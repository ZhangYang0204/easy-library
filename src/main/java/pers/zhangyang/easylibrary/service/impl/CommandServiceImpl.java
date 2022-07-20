package pers.zhangyang.easylibrary.service.impl;

import pers.zhangyang.easylibrary.base.DaoBase;
import pers.zhangyang.easylibrary.service.CommandService;
import pers.zhangyang.easylibrary.util.ResourceUtil;

import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.List;

public class CommandServiceImpl implements CommandService {
    public static final CommandServiceImpl INSTANCE = new CommandServiceImpl();

    @Override
    public void initDatabase() throws SQLException {
        List<Class> classList = ResourceUtil.getClasssFromJarFile();
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
