package pers.zhangyang.easylibrary.util;

import org.bukkit.Bukkit;

public class VersionUtil {

    public static int getMinecraftBigVersion() {
        return Integer.parseInt(Bukkit.getBukkitVersion().split("\\.")[0]);
    }

    public static int getMinecraftMiddleVersion() {
        return Integer.parseInt(Bukkit.getBukkitVersion().split("\\.")[1].split("-")[0]);
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
