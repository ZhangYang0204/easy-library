package pers.zhangyang.easylibrary.service.impl;

import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.dao.VersionDao;
import pers.zhangyang.easylibrary.meta.VersionMeta;
import pers.zhangyang.easylibrary.service.BaseService;

import java.sql.SQLException;

public class BaseServiceImpl implements BaseService {

    public static final BaseServiceImpl INSTANCE = new BaseServiceImpl();

    @Override
    public void initDatabase() throws SQLException {
        VersionDao.INSTANCE.init();
        if (VersionDao.INSTANCE.get() == null) {
            String[] strings = EasyPlugin.instance.getDescription().getVersion().split("\\.");
            VersionDao.INSTANCE.insert(new VersionMeta(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2])));
        }
    }


}
