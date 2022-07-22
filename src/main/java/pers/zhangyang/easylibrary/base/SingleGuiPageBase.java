package pers.zhangyang.easylibrary.base;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.exception.NotExistBackPageException;

public abstract class SingleGuiPageBase implements GuiPage {
    protected Inventory inventory;
    protected Player viewer;
    protected GuiPage backPage;

    public SingleGuiPageBase(@Nullable String title, Player viewer,GuiPage backPage){
        if (title!=null){
            inventory= Bukkit.createInventory(this,54, ChatColor.translateAlternateColorCodes('&', title));
        }else {
            inventory=Bukkit.createInventory(this,54);
        }
        this.viewer=viewer;
        this.backPage =backPage;
        send();
    }

    public abstract void send();
    public void backPage() throws NotExistBackPageException {
        backPage.send();
    }
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @NotNull
    public Player getViewer() {
        return viewer;
    }
}
