package pers.zhangyang.easylibrary.base;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.util.InventoryUtil;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.util.Collections;
import java.util.List;

public abstract class ExecutorBase {


    protected CommandSender sender;

    protected String[] args;

    protected String commandName;
    public ExecutorBase(@NotNull CommandSender sender,String commandName, @NotNull String[] args) {
        this.sender = sender;
        this.commandName=commandName;
        this.args = args;
    }

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
    protected boolean hasItemInMainHand() {
        Player player= (Player) sender;
        return !InventoryUtil.getItemInMainHand(player).getType().equals(Material.AIR);
    }
    protected boolean isPlayer() {
        return sender instanceof Player;
    }
    protected void notPlayer() {
        MessageUtil.sendMessageTo(sender,MessageYaml.INSTANCE.getStringList("message.chat.notPlayer"));
    }
    protected void notItemInMainHand() {
        MessageUtil.sendMessageTo(sender,MessageYaml.INSTANCE.getStringList("message.chat.notItemInMainHand"));
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

