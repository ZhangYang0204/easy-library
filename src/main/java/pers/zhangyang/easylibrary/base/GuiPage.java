package pers.zhangyang.easylibrary.base;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface GuiPage extends InventoryHolder {
    void send();

    void refresh();

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
