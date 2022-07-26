package pers.zhangyang.easylibrary.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandUtil {
    /**
     * 使commandSender以某些身份执行代码,cmdList的String格式需要为方式:命令，方式有console、self、operator，不符合的将跳过
     * 命令无需加/，如果是玩家支持Papi变量
     */
    public static void dispatchCommandList(@NotNull CommandSender commandSender, @NotNull List<String> cmdList) {
        for (String s : cmdList) {
            if (commandSender instanceof Player &&
                    Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                s = PlaceholderAPI.setPlaceholders((Player) commandSender, s);
            }

            s = ChatColor.translateAlternateColorCodes('&', s);
            String[] args = s.split(":");
            if (args.length != 2) {
                continue;
            }
            String way = args[0];
            String command = args[1];
            if ("console".equalsIgnoreCase(way)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                continue;
            }
            if ("self".equalsIgnoreCase(way)) {
                Bukkit.dispatchCommand(commandSender, command);
                continue;
            }
            if ("operator".equalsIgnoreCase(way)) {
                boolean op = commandSender.isOp();
                commandSender.setOp(true);
                Bukkit.dispatchCommand(commandSender, command);
                commandSender.setOp(op);
            }
        }
    }
}
