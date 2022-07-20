package pers.zhangyang.easylibrary.executor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.service.CommandService;
import pers.zhangyang.easylibrary.service.impl.CommandServiceImpl;
import pers.zhangyang.easylibrary.yaml.MessageYaml;
import pers.zhangyang.easylibrary.yaml.SettingYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;
import pers.zhangyang.easylibrary.yaml.CompleterYaml;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;

import java.io.IOException;
import java.sql.SQLException;

public class ReloadPlugin extends ExecutorBase {


    public ReloadPlugin(@NotNull CommandSender sender, boolean forcePlayer, String commandName, @NotNull String[] args) {
        super(sender, forcePlayer, commandName, args);
    }

    @Override
    protected void run() {

        if (args.length != 0) {
            return;
        }
        try {
            SettingYaml.INSTANCE.init();
            MessageYaml.INSTANCE.init();
            CompleterYaml.INSTANCE.init();
            DatabaseYaml.INSTANCE.init();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }
        //初始化数据库
        CommandService guiService = (CommandService) new TransactionInvocationHandler(CommandServiceImpl.INSTANCE).getProxy();

        try {
            guiService.initDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
