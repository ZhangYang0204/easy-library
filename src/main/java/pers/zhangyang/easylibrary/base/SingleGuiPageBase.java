package pers.zhangyang.easylibrary.base;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SingleGuiPageBase implements GuiPage {
    protected Inventory inventory;
    protected Player viewer;

    public SingleGuiPageBase(@Nullable String title, Player viewer){
        if (title!=null){
            inventory= Bukkit.createInventory(this,54,title);
        }else {
            inventory=Bukkit.createInventory(this,54);
        }
        this.viewer=viewer;
        refresh();
    }
    public void send(){
        refresh();
        viewer.openInventory(inventory);
    }
    public abstract void refresh();

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
