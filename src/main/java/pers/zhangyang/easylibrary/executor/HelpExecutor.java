package pers.zhangyang.easylibrary.executor;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.MessageUtil;

public class HelpExecutor extends ExecutorBase {


    public HelpExecutor(@NotNull CommandSender sender, boolean forcePlayer, String commandName, @NotNull String[] args) {
        super(sender, forcePlayer, commandName, args);
    }

    @Override
    protected void run() {
        if (args.length != 0) {
            return;
        }
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.help"));
    }
}
