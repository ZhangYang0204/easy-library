package pers.zhangyang.easylibrary.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MessageUtil {
    /**
     *     如果是玩家支持Papi变量
     */
    public static void notPlayer(CommandSender sender) {
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notPlayer"));
    }
    /**
     *     如果是玩家支持Papi变量
     */
    public static void notItemInMainHand(CommandSender sender) {
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notItemInMainHand"));
    }
    /**
     *     如果是玩家支持Papi变量
     */
    public static void invalidArgument(@NotNull CommandSender sender, String arg) {
        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.invalidArgument");
        if (list != null) {
            ReplaceUtil.replace(list, Collections.singletonMap("{argument}", arg));
        }
        MessageUtil.sendMessageTo(sender, list);
    }
    /**
     *     如果是玩家支持Papi变量
     */

    public static void sendTitleTo(@NotNull Player player, @Nullable String title, @Nullable String subtitle) {
        if (title != null) {

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null){
                title = PlaceholderAPI.setPlaceholders(player, title);
            }
            title = ChatColor.translateAlternateColorCodes('&', title);
        }
        if (subtitle != null) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null) {
                subtitle = PlaceholderAPI.setPlaceholders(player, subtitle);
            }
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
        }
        player.sendTitle(title, subtitle, 10, 10, 20);
    }
    /**
     *     如果是玩家支持Papi变量
     */
    public static void sendMessageTo(@NotNull CommandSender sender, @Nullable List<String> strings) {


        if (strings == null) {
            return;
        }
        for (String s : strings) {
            if (sender instanceof Player) {
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null) {
                    s = PlaceholderAPI.setPlaceholders((Player) sender, s);

                }
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }
    /**
     *     如果是玩家支持Papi变量
     */
    public static void sendMessageTo(@NotNull Collection<? extends CommandSender> senderList, @Nullable List<String> strings) {
        if (strings == null) {
            return;
        }
        for (CommandSender sender : senderList) {
            for (String s : strings) {
                if (sender instanceof Player) {
                    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null) {
                        s = PlaceholderAPI.setPlaceholders((Player) sender, s);
                    }

                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
        }
    }
    /**
     *     如果是玩家支持Papi变量
     */
    public static void sendMessageTo(@NotNull Collection<? extends CommandSender> senderList, @Nullable String s) {
        if (s == null) {
            return;
        }
        for (CommandSender sender : senderList) {
            if (sender instanceof Player) {
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null) {
                    s = PlaceholderAPI.setPlaceholders((Player) sender, s);
                }
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }
    /**
     *     如果是玩家支持Papi变量
     */
    public static void sendMessageTo(@NotNull CommandSender sender, @Nullable String s) {
        if (s == null) {
            return;
        }
        if (sender instanceof Player) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null) {
                s = PlaceholderAPI.setPlaceholders((Player) sender, s);
            }
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }

}
