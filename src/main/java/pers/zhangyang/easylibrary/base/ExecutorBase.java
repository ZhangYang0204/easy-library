package pers.zhangyang.easylibrary.base;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.util.Collections;
import java.util.List;

public abstract class ExecutorBase {

    protected boolean forcePlayer;

    protected CommandSender sender;

    protected String[] args;

    protected String commandName;
    public ExecutorBase(@NotNull CommandSender sender, boolean forcePlayer,String commandName, @NotNull String[] args) {
        this.sender = sender;
        this.commandName=commandName;
        this.forcePlayer = forcePlayer;
        this.args = args;
    }

    public void process() {
        if (!(sender instanceof Player) && forcePlayer) {
            MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notPlayer"));
            return;
        }
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

    protected void invalidArgument(@NotNull String arg) {
        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.invalidArgument");
        if (list != null) {
            ReplaceUtil.replace(list, Collections.singletonMap("{argument}", arg));
        }
        MessageUtil.sendMessageTo(sender, list);
    }

    protected abstract void run();

}

