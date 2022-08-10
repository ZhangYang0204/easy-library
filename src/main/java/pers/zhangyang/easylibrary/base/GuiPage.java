package pers.zhangyang.easylibrary.base;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.exception.NotExistBackPageException;

public interface GuiPage extends InventoryHolder {
    void send();

    void refresh();
    @NotNull
    Player getViewer();

    @NotNull
    OfflinePlayer getOwner();

    static void revoke() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getOpenInventory().getTopInventory().getHolder() instanceof GuiPage) {
                p.closeInventory();
            }
        }
    }

    @NotNull
     Inventory getInventory();
}
