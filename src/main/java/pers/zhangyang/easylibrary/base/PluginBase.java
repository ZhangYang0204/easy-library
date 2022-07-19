package pers.zhangyang.easylibrary.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PluginBase extends JavaPlugin {
    public static PluginBase instance;

    @Override
    public void onEnable() {
        instance=this;

    }


}
