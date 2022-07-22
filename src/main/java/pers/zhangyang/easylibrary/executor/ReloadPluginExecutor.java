package pers.zhangyang.easylibrary.executor;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.YamlBase;
import pers.zhangyang.easylibrary.service.CommandService;
import pers.zhangyang.easylibrary.service.impl.CommandServiceImpl;
import pers.zhangyang.easylibrary.util.ResourceUtil;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReloadPluginExecutor extends ExecutorBase {


    public ReloadPluginExecutor(@NotNull CommandSender sender,  String commandName, @NotNull String[] args) {
        super(sender,  commandName, args);
    }

    @Override
    protected void run() {
        try {
            InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream("easyLibrary模板.yml");
            YamlConfiguration yamlConfiguration=new YamlConfiguration();
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
                yamlBase.init();
            }
        } catch (NoSuchFieldException | IllegalAccessException | IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        GuiPage.revoke();
        //初始化数据库
        CommandService guiService = (CommandService) new TransactionInvocationHandler(new CommandServiceImpl()).getProxy();
        guiService.initDatabase();

    }
}
