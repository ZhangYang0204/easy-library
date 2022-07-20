package pers.zhangyang.easylibrary.executor;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.service.CommandService;
import pers.zhangyang.easylibrary.service.impl.CommandServiceImpl;
import pers.zhangyang.easylibrary.yaml.MessageYaml;
import pers.zhangyang.easylibrary.yaml.SettingYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;
import pers.zhangyang.easylibrary.yaml.CompleterYaml;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;

import java.io.IOException;
import java.sql.SQLException;

public class ReloadPluginExecutor extends ExecutorBase {


    public ReloadPluginExecutor(@NotNull CommandSender sender,  String commandName, @NotNull String[] args) {
        super(sender,  commandName, args);
    }

    @Override
    protected void run() {
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
