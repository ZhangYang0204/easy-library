package pers.zhangyang.easylibrary.util;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.Nullable;

public class PermUtil {
    @Nullable
    public static Integer getMaxNumberPerm(String startWith, Player player2) {
        Integer max = null;
        for (PermissionAttachmentInfo player : player2.getEffectivePermissions()) {
            if (!player.getPermission().startsWith(startWith.toLowerCase())) {
                continue;
            }
                int endIndex = player.getPermission().split("\\.").length-1;
                if (endIndex < 0) {
                    continue;
                }
                int current;
                    try {
                        current = Integer.parseInt(player.getPermission().split("\\.")[endIndex]);
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    if (max!=null&&current<=max) {
                        continue;
                    }
                    max=current;

        }
        return max;
    }

    @Nullable
    public static Integer getMinNumberPerm(String startWith, Player player2) {
        Integer min = null;
        for (PermissionAttachmentInfo player : player2.getEffectivePermissions()) {
            if (!player.getPermission().startsWith(startWith.toLowerCase())) {
                continue;
            }
            int endIndex = player.getPermission().split("\\.").length-1;
            if (endIndex < 0) {
                continue;
            }
            int current;
            try {
                current = Integer.parseInt(player.getPermission().split("\\.")[endIndex]);
            } catch (NumberFormatException e) {
                continue;
            }

            if (min!=null&&current>=min) {
                continue;
            }
            min=current;

        }
        return min;
    }
}
