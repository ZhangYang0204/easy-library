package pers.zhangyang.easylibrary;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.NotifyVersionUtil;
import pers.zhangyang.easylibrary.util.ResourceUtil;
import pers.zhangyang.easylibrary.yaml.CompleterYaml;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;
import pers.zhangyang.easylibrary.yaml.MessageYaml;
import pers.zhangyang.easylibrary.yaml.SettingYaml;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class EasyPlugin extends JavaPlugin {
    public static EasyPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        try {
            SettingYaml.INSTANCE.init();
            DatabaseYaml.INSTANCE.init();
            CompleterYaml.INSTANCE.init();
            MessageYaml.INSTANCE.init();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            this.setEnabled(false);
        }
        onOpen();

        try {
            automaticRegisterListener();
        } catch (Exception e) {
            e.printStackTrace();
            this.setEnabled(false);
        }

        NotifyVersionUtil.notifyVersion(Bukkit.getConsoleSender());
        MessageUtil.sendMessageTo(Bukkit.getConsoleSender(), MessageYaml.INSTANCE.getStringList("message.chat.enablePlugin"));
    }

    private void automaticRegisterListener() throws Exception {
            List<Class> classList = ResourceUtil.getClasssFromJarFile();
            for (Class c : classList) {
                System.out.println(c+"1");
                if (!c.isAnnotationPresent(EventListener.class)) {
                    continue;
                }
                if (Modifier.isInterface(c.getModifiers()) || Modifier.isAbstract(c.getModifiers())) {
                    continue;
                }
                if (!Listener.class.isAssignableFrom(c)) {
                    continue;
                }
                System.out.println(c);
                Listener listener = (Listener) c.newInstance();
                Bukkit.getPluginManager().registerEvents(listener, EasyPlugin.instance);
            }

    }

    @Override
    public void onDisable() {
        onClose();
        MessageUtil.sendMessageTo(Bukkit.getConsoleSender(), MessageYaml.INSTANCE.getStringList("message.chat.disablePlugin"));
    }

    public abstract void onOpen();

    public abstract void onClose();

    public abstract void onExecutor(@NotNull CommandSender sender, String commandName, @NotNull String[] argument);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return true;
        }
        String[] argument = new String[args.length - 1];
        System.arraycopy(args, 1, argument, 0, args.length - 1);
        onExecutor(sender, args[0], argument);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list;

        char[] pluginNameChars = getName().toCharArray();
        pluginNameChars[0] = (char) (pluginNameChars[0] + 32);
        StringBuilder key = new StringBuilder(String.valueOf(pluginNameChars));

        if (args.length == 1) {
            list = CompleterYaml.INSTANCE.getStringList("completer." + key);
            String ll = args[0].toLowerCase();
            if (list != null) {
                list.removeIf(k -> !k.toLowerCase().startsWith(ll));
            }
            if (list == null) {
                return new ArrayList<>();
            }
            return list;
        }
        if (!sender.hasPermission(args[0])) {
            return new ArrayList<>();
        }

        char[] chars = args[0].toCharArray();
        chars[0] = (char) (pluginNameChars[0] + 32);
        key.append(chars[0]);

        for (int i = 0; i < alias.length() - 1; i++) {
            key.append("$");
        }
        list = CompleterYaml.INSTANCE.getStringList("completer." + key);
        String ll = args[0].toLowerCase();
        if (list != null) {
            list.removeIf(k -> !k.toLowerCase().startsWith(ll));
        }
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }
}
