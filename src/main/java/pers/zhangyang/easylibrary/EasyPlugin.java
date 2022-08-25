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
import pers.zhangyang.easylibrary.base.YamlBase;
import pers.zhangyang.easylibrary.executor.CorrectYamlExecutor;
import pers.zhangyang.easylibrary.executor.HelpExecutor;
import pers.zhangyang.easylibrary.executor.ReloadPluginExecutor;
import pers.zhangyang.easylibrary.listener.PlayerClickGuiPage;
import pers.zhangyang.easylibrary.listener.PlayerJoin;
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
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class EasyPlugin extends JavaPlugin {
    public static EasyPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        //加载Yaml类，自动init他们
        try {
            SettingYaml.INSTANCE.init();
            CompleterYaml.INSTANCE.init();
            DatabaseYaml.INSTANCE.init();
            MessageYaml.INSTANCE.init();
            InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream("easyLibrary.yml");
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            assert in != null;
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            yamlConfiguration.load(inputStreamReader);
            List<Class> classList = ResourceUtil.getClassesFromJarFile(yamlConfiguration.getStringList("yamlPackage"));
            for (Class c : classList) {
                if (Modifier.isInterface(c.getModifiers()) || Modifier.isAbstract(c.getModifiers())) {
                    continue;
                }
                if (!YamlBase.class.isAssignableFrom(c)) {
                    continue;
                }
                Class.forName(c.getName());
            }
        } catch (IOException | InvalidConfigurationException | ClassNotFoundException | URISyntaxException e) {
            throw new RuntimeException(e);
        }


        //必须先open再init
        onOpen();

        //自动初始化全部的数据库
        BaseService baseService = (BaseService) new TransactionInvocationHandler(new BaseServiceImpl()).getProxy();
        baseService.initDatabase();

        //自动注册有注解的监听器
        Bukkit.getPluginManager().registerEvents(new PlayerClickGuiPage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        try {
            InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream("easyLibrary.yml");
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            assert in != null;
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            yamlConfiguration.load(inputStreamReader);
            List<Class> classList = ResourceUtil.getClassesFromJarFile(yamlConfiguration.getStringList("listenerPackage"));
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
        } catch (InstantiationException | IllegalAccessException | IOException | InvalidConfigurationException |
                 URISyntaxException e) {
            throw new RuntimeException(e);
        }

        //通知版本信息
        NotifyVersionUtil.notifyVersion(Bukkit.getConsoleSender());

        //提示插件标志
        MessageUtil.sendMessageTo(Bukkit.getConsoleSender(), MessageYaml.INSTANCE.getStringList("message.chat.enablePlugin"));
    }


    @Override
    public void onDisable() {
        //关闭所有Gui
        GuiPage.revoke();
        onClose();
        //提示插件关闭
        MessageUtil.sendMessageTo(Bukkit.getConsoleSender(), MessageYaml.INSTANCE.getStringList("message.chat.disablePlugin"));
    }

    public abstract void onOpen();

    public abstract void onClose();

    //自动调度全部命令
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return true;
        }


        String[] argument = new String[args.length - 1];
        System.arraycopy(args, 1, argument, 0, args.length - 1);
//注册内部的
        if (args[0].equalsIgnoreCase("CorrectYaml")) {
            new CorrectYamlExecutor(sender, args[0], argument).process();
        }
        if (args[0].equalsIgnoreCase("Help")) {
            new HelpExecutor(sender, args[0], argument).process();
        }
        if (args[0].equalsIgnoreCase("ReloadPlugin")) {
            new ReloadPluginExecutor(sender, args[0], argument).process();
        }
        try {

            InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream("easyLibrary.yml");
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            assert in != null;
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            yamlConfiguration.load(inputStreamReader);
            List<Class> classList = ResourceUtil.getClassesFromJarFile(yamlConfiguration.getStringList("executorPackage"));
            for (Class c : classList) {
                if (Modifier.isInterface(c.getModifiers()) || Modifier.isAbstract(c.getModifiers())) {
                    continue;
                }
                if (!ExecutorBase.class.isAssignableFrom(c)) {
                    continue;
                }

                Constructor<ExecutorBase> constructor = c.getDeclaredConstructor(CommandSender.class, String.class, String[].class);
                constructor.setAccessible(true);
                ExecutorBase executorBase = constructor.newInstance(sender, args[0], argument);
                String name = executorBase.getClass().getName();
                name = name.substring(0, name.length() - 8);
                name = name.split("\\.")[name.split("\\.").length - 1];
                if (!name.equalsIgnoreCase(args[0])) {
                    continue;
                }
                executorBase.process();
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                 IOException | InvalidConfigurationException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return true;
    }


    //自动提示命令
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 0) {
            return new ArrayList<>();
        }

        List<String> list;

        char[] pluginNameChars = getName().toCharArray();
        pluginNameChars[0] = (Character.toLowerCase(pluginNameChars[0]));
        StringBuilder key = new StringBuilder(String.valueOf(pluginNameChars));


        if (args.length == 1) {
            list = CompleterYaml.INSTANCE.getStringList("completer." + key);
            String ll = args[args.length - 1].toLowerCase();
            if (list != null) {
                list.removeIf(k -> !k.toLowerCase().startsWith(ll));
            }
            if (list == null) {
                return new ArrayList<>();
            }
            return list;
        }
        if (!sender.hasPermission(EasyPlugin.instance.getName() + "." + args[0])) {
            return new ArrayList<>();
        }

        char[] chars = args[0].toCharArray();
        if (chars.length != 0) {
            chars[0] = (Character.toUpperCase(chars[0]));
        }
        key.append(chars);


        for (int i = 0; i < args.length - 2; i++) {
            key.append("$");
        }
        list = CompleterYaml.INSTANCE.getStringList("completer." + key);
        String ll = args[args.length - 1].toLowerCase();
        if (list != null) {
            list.removeIf(k -> !k.toLowerCase().startsWith(ll));
        }
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }
}
