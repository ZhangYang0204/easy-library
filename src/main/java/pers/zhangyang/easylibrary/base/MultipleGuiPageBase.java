package pers.zhangyang.easylibrary.base;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MultipleGuiPageBase implements GuiPage {
    protected Inventory inventory;
    protected Player viewer;
    protected int pageIndex;

    public MultipleGuiPageBase(@Nullable String title, Player viewer){
        if (title!=null){
            inventory= Bukkit.createInventory(this,54,title);
        }else {
            inventory=Bukkit.createInventory(this,54);
        }
        this.viewer=viewer;
        this.pageIndex=0;
        refresh();
    }
    public void send(){
        refresh();
        viewer.openInventory(inventory);
    }
    public abstract void refresh();

    public  void nextPage(){
        this.pageIndex++;
        refresh();
    }

    public  void previousPage(){
        this.pageIndex--;
        refresh();
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
