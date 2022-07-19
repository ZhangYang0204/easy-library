package pers.zhangyang.easylibrary.base;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class EasyPluginBase extends JavaPlugin {
    public static EasyPluginBase instance;

    @Override
    public void onEnable() {
        instance=this;
    }


}
