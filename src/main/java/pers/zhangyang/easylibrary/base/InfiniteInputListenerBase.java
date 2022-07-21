package pers.zhangyang.easylibrary.base;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.util.ArrayList;
import java.util.List;

public abstract class InfiniteInputListenerBase implements Listener {
    Player player;
    GuiPage previousPage;
    List<String> messageList =new ArrayList<>();
    public InfiniteInputListenerBase(Player player, GuiPage previousPage) {
        this.player = player;
        this.previousPage = previousPage;

    }


    public void on(AsyncPlayerChatEvent event){
        if (!event.getPlayer().equals(player)){
            return;
        }
        event.setCancelled(true);
        if (event.getMessage().equalsIgnoreCase(MessageYaml.INSTANCE.getInput("message.input.cancel"))){
            previousPage.send();
            return;
        }
        if (event.getMessage().equalsIgnoreCase(MessageYaml.INSTANCE.getInput("message.input.submit"))){
            previousPage.send();
            AsyncPlayerChatEvent.getHandlerList().unregister(EasyPlugin.instance);
            PlayerQuitEvent.getHandlerList().unregister(EasyPlugin.instance);
            new BukkitRunnable() {
                @Override
                public void run() {
                    run();
                }
            }.runTask(EasyPlugin.instance);
            return;
        }
        messageList.add(event.getMessage());

    }

    public abstract void run();
}
