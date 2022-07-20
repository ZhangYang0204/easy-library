package pers.zhangyang.easylibrary.util;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class NotifyVersionUtil {


    public static void notifyVersion(CommandSender sender) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String latestVersion;
                try {
                    String urlStringPart1="https://zhangyang0204.github.io/";
                    String urlStringPart3="/index.html";
                    String urlStringPart2=ReplaceUtil.replaceToRepositoryName(EasyPlugin.instance.getName());
                    latestVersion=ResourceUtil.readFirstLine(new URL(urlStringPart1+urlStringPart2+urlStringPart3));
                } catch (Throwable e) {
                    latestVersion = null;
                }
                String finalLatestVersion = latestVersion;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        List<String> list;
                        if (finalLatestVersion != null) {
                            list = MessageYaml.INSTANCE.getStringList("message.chat.notifyVersion");
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("{current_version}", EasyPlugin.instance.getDescription().getVersion());
                            hashMap.put("{latest_version}", finalLatestVersion);
                            if (list != null) {
                                ReplaceUtil.replace(list, hashMap);
                            }
                        } else {
                            list = MessageYaml.INSTANCE.getStringList("message.chat.failureGetLatestVersion");
                        }
                        MessageUtil.sendMessageTo(sender, list);
                    }
                }.runTask(EasyPlugin.instance);

            }
        }.runTaskAsynchronously(EasyPlugin.instance);

    }

}
