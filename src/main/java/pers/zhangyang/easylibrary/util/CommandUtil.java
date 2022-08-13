package pers.zhangyang.easylibrary.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandUtil {

    public void dispatchCommandList(@NotNull CommandSender commandSender,@NotNull List<String> cmdList){
        for (String s : cmdList) {
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
            if ("self".equalsIgnoreCase(way)&&commandSender instanceof Player) {
                Bukkit.dispatchCommand(commandSender, command);
                continue;
            }
            if ("operator".equalsIgnoreCase(way)&&commandSender instanceof Player) {
                boolean op=commandSender.isOp();
                commandSender.setOp(true);
                Bukkit.dispatchCommand(commandSender, command);
                commandSender.setOp(op);
            }
        }
    }
}