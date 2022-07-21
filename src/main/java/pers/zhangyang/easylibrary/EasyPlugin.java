package pers.zhangyang.easylibrary;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.executor.CorrectYamlExecutor;
import pers.zhangyang.easylibrary.executor.HelpExecutor;
import pers.zhangyang.easylibrary.executor.ReloadPluginExecutor;
import pers.zhangyang.easylibrary.service.BaseService;
import pers.zhangyang.easylibrary.service.impl.BaseServiceImpl;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.NotifyVersionUtil;
import pers.zhangyang.easylibrary.util.ResourceUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;
import pers.zhangyang.easylibrary.yaml.CompleterYaml;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;
import pers.zhangyang.easylibrary.yaml.MessageYaml;
import pers.zhangyang.easylibrary.yaml.SettingYaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        BaseService baseService= (BaseService) new TransactionInvocationHandler(BaseServiceImpl.INSTANCE).getProxy();

        //必须先open再init
        onOpen();
        try {
            baseService.initDatabase();
        } catch (SQLException | IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            this.setEnabled(false);
        }
        try {

            InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream("easyLibrary.yml");
            YamlConfiguration yamlConfiguration=new YamlConfiguration();
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            yamlConfiguration.load(inputStreamReader);
            List<Class> classList = ResourceUtil.getClasssFromJarFile(yamlConfiguration.getStringList("listenerPackage"));
            for (Class c : classList) {
                if (!c.isAnnotationPresent(EventListener.class)) {
                    continue;
                }
                if (Modifier.isInterface(c.getModifiers()) || Modifier.isAbstract(c.getModifiers())) {
                    continue;
                }
                if (!Listener.class.isAssignableFrom(c)) {
                    continue;
                }
                Listener listener = (Listener) c.newInstance();
                Bukkit.getPluginManager().registerEvents(listener, EasyPlugin.instance);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            this.setEnabled(false);
        }

        NotifyVersionUtil.notifyVersion(Bukkit.getConsoleSender());
        MessageUtil.sendMessageTo(Bukkit.getConsoleSender(), MessageYaml.INSTANCE.getStringList("message.chat.enablePlugin"));
    }


    @Override
    public void onDisable() {
        GuiPage.revoke();
        onClose();
        MessageUtil.sendMessageTo(Bukkit.getConsoleSender(), MessageYaml.INSTANCE.getStringList("message.chat.disablePlugin"));
    }

    public abstract void onOpen();

    public abstract void onClose();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return true;
        }

        String[] argument = new String[args.length - 1];
        System.arraycopy(args, 1, argument, 0, args.length - 1);


        try {

            InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream("easyLibrary.yml");
            YamlConfiguration yamlConfiguration=new YamlConfiguration();
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            yamlConfiguration.load(inputStreamReader);
            List<Class> classList = ResourceUtil.getClasssFromJarFile(yamlConfiguration.getStringList("executorPackage"));
            for (Class c : classList) {
                if (Modifier.isInterface(c.getModifiers()) || Modifier.isAbstract(c.getModifiers())) {
                    continue;
                }
                if (!ExecutorBase.class.isAssignableFrom(c)) {
                    continue;
                }

                Constructor<ExecutorBase> constructor=c.getDeclaredConstructor(CommandSender.class,String.class,String[].class);
                constructor.setAccessible(true);
                ExecutorBase executorBase =  constructor.newInstance(sender,args[0],argument);
                String name=executorBase.getClass().getName();
                name=name.substring(0,name.length()-8);
                name=name.split("\\.")[name.split("\\.").length-1];
                if (!name.equalsIgnoreCase(args[0])){
                    continue;
                }
                executorBase.process();
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

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
