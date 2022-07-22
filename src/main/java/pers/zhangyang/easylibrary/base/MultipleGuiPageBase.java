package pers.zhangyang.easylibrary.base;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.exception.NotExistBackPageException;
import pers.zhangyang.easylibrary.exception.NotExistNextException;
import pers.zhangyang.easylibrary.exception.NotExistPreviousException;

public abstract class MultipleGuiPageBase implements GuiPage {
    protected Inventory inventory;
    protected Player viewer;
    protected int pageIndex;
    protected GuiPage backPage;

    public MultipleGuiPageBase(@Nullable String title,@NotNull Player viewer,@Nullable GuiPage backPage){
        if (title!=null){
            inventory= Bukkit.createInventory(this,54, ChatColor.translateAlternateColorCodes('&', title));
        }else {
            inventory=Bukkit.createInventory(this,54);
        }
        this.viewer=viewer;
        this.pageIndex=0;
        this.backPage = backPage;
        send();
    }
    public abstract void send();

    public  void nextPage() throws NotExistNextException {
        this.pageIndex++;
        send();
    }

    public  void previousPage() throws NotExistPreviousException {
        this.pageIndex--;
        send();
    }

    public void backPage() throws  NotExistBackPageException{
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
