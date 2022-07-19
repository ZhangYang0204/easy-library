package pers.zhangyang.easylibrary.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class EasyPluginBase extends JavaPlugin {
    public static EasyPluginBase instance;

    @Override
    public void onEnable() {
        instance=this;
        onOpen();
    }
    @Override
    public void onDisable() {
        onClose();
    }
    public abstract void onOpen();

    public abstract void onClose();
    public abstract void onExecutor(@NotNull CommandSender sender,  String commandName, @NotNull String[] argument);

    @Nullable
    public abstract List<String> onArgumentCompleter(@NotNull CommandSender sender, String commandName, @NotNull String[] argument);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==0){
            return true;
        }
        String[] argument=new String[args.length-1];
        System.arraycopy(args, 1, argument, 0, args.length - 1);
        onExecutor(sender,args[0],argument);
        return true;
    }
    @Nullable
    public abstract List<String> onCommandCompleter(@NotNull CommandSender sender);

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length==0){
            return onCommandCompleter(sender);
        }
        String[] argument=new String[args.length-1];
        System.arraycopy(args, 1, argument, 0, args.length - 1);
        return onArgumentCompleter(sender,args[0],argument);
    }
}
