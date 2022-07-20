package pers.zhangyang.easylibrary.executor;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.MessageUtil;

public class HelpExecutor extends ExecutorBase {


    public HelpExecutor(@NotNull CommandSender sender,  String commandName, @NotNull String[] args) {
        super(sender, commandName, args);
    }

    @Override
    protected void run() {
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.help"));
    }
}
