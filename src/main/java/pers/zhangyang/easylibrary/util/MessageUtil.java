package pers.zhangyang.easylibrary.util;

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
    public static void notPlayer(CommandSender sender) {
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notPlayer"));
    }

    public static void notItemInMainHand(CommandSender sender) {
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notItemInMainHand"));
    }

    public static void invalidArgument(@NotNull CommandSender sender, String arg) {
        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.invalidArgument");
        if (list != null) {
            ReplaceUtil.replace(list, Collections.singletonMap("{argument}", arg));
        }
        MessageUtil.sendMessageTo(sender, list);
    }

    public static void sendTitleTo(@NotNull Player player, @Nullable String title, @Nullable String subtitle) {
        if (title != null) {
            title = ChatColor.translateAlternateColorCodes('&', title);
        }
        if (subtitle != null) {
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
        }
        player.sendTitle(title, subtitle, 10, 10, 20);
    }

    public static void sendMessageTo(@NotNull CommandSender sender, @Nullable List<String> strings) {
        if (strings == null) {
            return;
        }
        for (String s : strings) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }

    public static void sendMessageTo(@NotNull Collection<? extends CommandSender> senderList, @Nullable List<String> strings) {
        if (strings == null) {
            return;
        }
        for (CommandSender sender : senderList) {
            for (String s : strings) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
        }
    }

    public static void sendMessageTo(@NotNull Collection<? extends CommandSender> senderList, @Nullable String s) {
        if (s == null) {
            return;
        }
        for (CommandSender sender : senderList) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }

    public static void sendMessageTo(@NotNull CommandSender sender, @Nullable String s) {
        if (s == null) {
            return;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }

}
