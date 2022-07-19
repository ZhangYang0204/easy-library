package pers.zhangyang.easylibrary.base;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;

import java.util.Collections;
import java.util.List;

public abstract class ExecutorBase {

    protected boolean forcePlayer;

    protected CommandSender sender;

    protected String[] args;

    public ExecutorBase(@NotNull CommandSender sender, boolean forcePlayer, @NotNull String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException();
        }
        this.sender = sender;
        this.forcePlayer = forcePlayer;
        this.args = args;
    }

    public void process() {
        if (!(sender instanceof Player) && forcePlayer) {
            MessageUtil.sendMessageTo(sender, getNotPlayerMessage());
            return;
        }
        String permission = "EasyGuiShop." + args[0];
        if (!sender.hasPermission(permission)) {
            List<String> list = getNotPermissionMessage();
            if (list != null) {
                ReplaceUtil.replace(list, Collections.singletonMap("{permission}", permission));
            }
            MessageUtil.sendMessageTo(sender, list);
            return;
        }
        run();
    }

    protected void invalidArgument(@NotNull String arg) {
        List<String> list = getInvalidArgumentMessage();
        if (list != null) {
            ReplaceUtil.replace(list, Collections.singletonMap("{argument}", arg));
        }
        MessageUtil.sendMessageTo(sender, list);
    }
    @Nullable
    public  List<String> getNotPlayerMessage(){
        return null;
    }

    @Nullable
    public  List<String> getNotPermissionMessage(){
        return null;
    }
    @Nullable
    public  List<String> getInvalidArgumentMessage(){
        return null;
    }

    protected abstract void run();

}

