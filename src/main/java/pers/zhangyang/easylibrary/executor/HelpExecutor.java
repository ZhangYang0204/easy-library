package pers.zhangyang.easylibrary.executor;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.util.Collections;
import java.util.List;

public class HelpExecutor {
    protected CommandSender sender;

    protected String[] args;

    protected String commandName;

    public HelpExecutor(@NotNull CommandSender sender, String commandName, @NotNull String[] args) {
        this.sender = sender;
        this.commandName = commandName;
        this.args = args;
    }

    public void process() {
        String permission = EasyPlugin.instance.getName() + "." + commandName;
        if (!sender.hasPermission(permission)) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notPermission");
            if (list != null) {
                ReplaceUtil.replace(list, Collections.singletonMap("{permission}", permission));
            }
            MessageUtil.sendMessageTo(sender, list);
            return;
        }
        run();
    }

    protected void run() {
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.help"));
    }
}
