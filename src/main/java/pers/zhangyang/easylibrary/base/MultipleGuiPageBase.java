package pers.zhangyang.easylibrary.base;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.exception.NotExistBackPageException;
import pers.zhangyang.easylibrary.exception.NotExistNextPageException;
import pers.zhangyang.easylibrary.exception.NotExistPreviousPageException;

public abstract class MultipleGuiPageBase implements GuiPage {
    protected Inventory inventory;
    protected Player viewer;
    protected int pageIndex;
    protected GuiPage backPage;
    protected OfflinePlayer owner;

    public MultipleGuiPageBase(@Nullable String title,@NotNull Player viewer,@Nullable GuiPage backPage,OfflinePlayer owner){
        if (title!=null){
            inventory= Bukkit.createInventory(this,54, ChatColor.translateAlternateColorCodes('&', title));
        }else {
            inventory=Bukkit.createInventory(this,54);
        }
        this.owner=owner;
        this.viewer=viewer;
        this.pageIndex=0;
        this.backPage = backPage;
    }
    public abstract void send();
    public abstract void refresh();

    public  void nextPage() throws NotExistNextPageException {
        this.pageIndex++;
        refresh();
    }

    public  void previousPage() throws NotExistPreviousPageException {
        this.pageIndex--;
        refresh();
    }

    public void backPage() throws  NotExistBackPageException{
        if (backPage==null){
            throw new NotExistBackPageException();
        }
        backPage.send();
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @NotNull
    @Override
    public Player getViewer() {
        return viewer;
    }

    @NotNull
    @Override
    public OfflinePlayer getOwner() {
        return owner;
    }
}
