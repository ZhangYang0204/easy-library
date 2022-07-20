package pers.zhangyang.easylibrary.executor;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.base.YamlBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.ResourceUtil;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CorrectYamlExecutor extends ExecutorBase {


    public CorrectYamlExecutor(@NotNull CommandSender sender,  String commandName, @NotNull String[] args) {
        super(sender,  commandName, args);
    }

    @Override
    protected void run() {

        try {
            InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream("easyLibrary.yml");
            YamlConfiguration yamlConfiguration=new YamlConfiguration();
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            yamlConfiguration.load(inputStreamReader);
            List<Class> classList = ResourceUtil.getClasssFromJarFile(yamlConfiguration.getStringList("yamlPackage"));
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
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }

        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.correctYaml"));
    }
}
