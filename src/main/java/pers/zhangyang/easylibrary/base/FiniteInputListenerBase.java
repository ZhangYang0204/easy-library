package pers.zhangyang.easylibrary.base;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

/**
 * 有限参数的Gui交互输入类的父类
 */
public abstract class FiniteInputListenerBase implements Listener {
    /**
     * 交互的玩家
     */
    protected Player player;
    /**
     * 返回哪一页
     */
    protected GuiPage previousPage;
    /**
     * 交互的信息
     */
    protected String[] messages;

    protected OfflinePlayer owner;

    /**
     * @param player       交互的玩家
     * @param previousPage 交互后返回的Gui
     * @param sequence     交互的信息的数量
     */
    public FiniteInputListenerBase(Player player,OfflinePlayer owner, GuiPage previousPage, int sequence) {
        this.player = player;
        this.owner=owner;
        this.previousPage = previousPage;
        this.messages = new String[sequence];
        Bukkit.getPluginManager().registerEvents(this, EasyPlugin.instance);
        player.closeInventory();
    }


    /**
     * 处理玩家输入取消事件，取消的触发内容在message.input.cancel中指定
     *
     * @param event
     */
    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }
        event.setCancelled(true);
        if (event.getMessage().equalsIgnoreCase(MessageYaml.INSTANCE.getInput("message.input.cancel"))) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    previousPage.refresh();
                }
            }.runTask(EasyPlugin.instance);
            return;
        }
        for (int i = 0; i < messages.length; i++) {
            if (messages[i] == null) {
                messages[i] = event.getMessage();
                break;
            }
        }
        if (messages[messages.length - 1] == null) {
            return;
        }

        AsyncPlayerChatEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
        new BukkitRunnable() {
            @Override
            public void run() {
                previousPage.refresh();
                FiniteInputListenerBase.this.run();
            }
        }.runTask(EasyPlugin.instance);
    }

    /**
     * 当交互完后，执行该方法的内容
     */
    public abstract void run();
}
