package pers.zhangyang.easylibrary.service;

import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.sql.SQLException;

public interface BaseService {
    void initDatabase() throws SQLException, IOException, InvalidConfigurationException;

}
