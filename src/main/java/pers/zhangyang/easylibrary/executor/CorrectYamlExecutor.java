package pers.zhangyang.easylibrary.executor;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.base.YamlBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.util.ResourceUtil;
import pers.zhangyang.easylibrary.yaml.CompleterYaml;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;
import pers.zhangyang.easylibrary.yaml.MessageYaml;
import pers.zhangyang.easylibrary.yaml.SettingYaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class CorrectYamlExecutor {
    protected CommandSender sender;

    protected String[] args;

    protected String commandName;

    public CorrectYamlExecutor(@NotNull CommandSender sender, String commandName, @NotNull String[] args) {
        this.sender = sender;
        this.commandName = commandName;
        this.args = args;
    }

    public void process() {
        String permission = EasyPlugin.instance.getName() + "." + commandName;
        if (!sender.hasPermission(permission)) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notPermission");
            if (list != null) {
                ReplaceUtil.replace(list, Collections.singletonMap("{permission}", permission));
            }
            MessageUtil.sendMessageTo(sender, list);
            return;
        }
        run();
    }

    protected void run() {

        try {
            CompleterYaml.INSTANCE.correct();
            DatabaseYaml.INSTANCE.correct();
            MessageYaml.INSTANCE.correct();
            SettingYaml.INSTANCE.correct();
            InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream("easyLibrary.yml");
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
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
                YamlBase yamlBase = (YamlBase) c.getField("INSTANCE").get(c);
                yamlBase.correct();
            }
        } catch (NoSuchFieldException | IllegalAccessException | IOException | InvalidConfigurationException |
                 URISyntaxException e) {
            throw new RuntimeException(e);
        }
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.correctYaml"));
    }
}
