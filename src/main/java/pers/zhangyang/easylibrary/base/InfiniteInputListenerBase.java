package pers.zhangyang.easylibrary.base;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.util.ArrayList;
import java.util.List;

public abstract class InfiniteInputListenerBase implements Listener {
    protected Player player;
    protected GuiPage previousPage;
    protected List<String> messageList =new ArrayList<>();
    public InfiniteInputListenerBase(Player player, GuiPage previousPage) {
        this.player = player;
        this.previousPage = previousPage;
        Bukkit.getPluginManager().registerEvents(this, EasyPlugin.instance);
        player.closeInventory();
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent event){
        if (!event.getPlayer().equals(player)){
            return;
        }
        event.setCancelled(true);
        if (event.getMessage().equalsIgnoreCase(MessageYaml.INSTANCE.getInput("message.input.cancel"))){
            new BukkitRunnable() {
                @Override
                public void run() {
                    previousPage.send();
                }
            }.runTask(EasyPlugin.instance);
            return;
        }
        if (!event.getMessage().equalsIgnoreCase(MessageYaml.INSTANCE.getInput("message.input.submit"))){
            messageList.add(event.getMessage());
            return;
        }
            AsyncPlayerChatEvent.getHandlerList().unregister(EasyPlugin.instance);
            PlayerQuitEvent.getHandlerList().unregister(EasyPlugin.instance);
            new BukkitRunnable() {
                @Override
                public void run() {
                    previousPage.refresh();
                    InfiniteInputListenerBase.this.run();
                }
            }.runTask(EasyPlugin.instance);

    }

    public abstract void run();
}
