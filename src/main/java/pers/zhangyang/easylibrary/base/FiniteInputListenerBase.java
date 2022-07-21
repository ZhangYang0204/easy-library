package pers.zhangyang.easylibrary.base;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

public abstract class FiniteInputListenerBase implements Listener {
    Player player;
    GuiPage previousPage;
    String[] messages;
    public FiniteInputListenerBase(Player player, GuiPage previousPage, int sequence) {
        this.player = player;
        this.previousPage = previousPage;
        this.messages=new String[sequence];

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
        if (messages[messages.length-1]==null){
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
        for (int i=0;i< messages.length;i++){
            if (messages[i]!=null){
                messages[i]=event.getMessage();
            }
        }

    }

    public abstract void run();
}
