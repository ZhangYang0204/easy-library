package pers.zhangyang.easylibrary.executor;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.base.YamlBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.ResourceUtil;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;

public class CorrectYamlExecutor extends ExecutorBase {


    public CorrectYamlExecutor(@NotNull CommandSender sender, boolean forcePlayer, String commandName, @NotNull String[] args) {
        super(sender, forcePlayer, commandName, args);
    }

    @Override
    protected void run() {
        if (args.length != 0) {
            return;
        }

        try {
            List<Class> classList = ResourceUtil.getClasssFromJarFile();
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
        } catch (IOException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        System.out.println(123);
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.correctYaml"));
    }
}
