package pers.zhangyang.easylibrary.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.util.NotifyVersionUtil;

public class PlayerJoin implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent event){
        Player player=event.getPlayer();
        if (!player.hasPermission(EasyPlugin.instance.getName()+".receiveVersionInformation")){
            return;
        }
        NotifyVersionUtil.notifyVersion(player);
    }
}
