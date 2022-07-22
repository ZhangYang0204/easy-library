package pers.zhangyang.easylibrary.base;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.util.Collections;
import java.util.List;

public abstract class ExecutorBase {


    protected CommandSender sender;

    protected String[] args;

    protected String commandName;

    public ExecutorBase(@NotNull CommandSender sender, String commandName, @NotNull String[] args) {
        this.sender = sender;
        this.commandName = commandName;
        this.args = args;
    }

    //执行命令，同时回判断有无权限
    public void process() {
        String permission = "EasyGuiShop." + commandName;
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


    protected abstract void run();

}

