package pers.zhangyang.easylibrary.util;

import org.bukkit.Bukkit;
import pers.zhangyang.easylibrary.EasyPlugin;

public class VersionUtil {

    public static int getMinecraftBigVersion() {
        return Integer.parseInt(Bukkit.getBukkitVersion().split("\\.")[0]);
    }

    public static int getMinecraftMiddleVersion() {
        return Integer.parseInt(Bukkit.getBukkitVersion().split("\\.")[1].split("-")[0]);
    }
    public static int getPluginBigVersion() {
        return Integer.parseInt(EasyPlugin.instance.getDescription().getVersion().split("\\.")[0]);
    }
    public static int getPluginMiddleVersion() {
        return Integer.parseInt(EasyPlugin.instance.getDescription().getVersion().split("\\.")[1]);
    }
    public static int getPluginSmallVersion() {
        return Integer.parseInt(EasyPlugin.instance.getDescription().getVersion().split("\\.")[2]);
    }
    public static boolean isNewerThan(int currentBig,int currentMiddle,int currentSmall,int big,int middle,int small){
        if (currentBig<big){
            return false;
        }
        if (currentBig>big){
            return true;
        }
        if (currentMiddle<middle){
            return false;
        }
        if (currentMiddle>middle){
            return true;
        }

        return currentSmall > small;
    }
    public static boolean isNewerThan(int currentBig,int currentMiddle,int big,int middle){
        if (currentBig<big){
            return false;
        }
        if (currentBig>big){
            return true;
        }

        return currentMiddle > middle;
    }
}
