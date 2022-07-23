package pers.zhangyang.easylibrary.base;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.util.Collections;
import java.util.List;

/**
 * 命令处理类的父类，继承该类的子类会在指令执行时自动被调用，前提是命名格式为命令名Executor，例如HelpExecutor，并且还要在easyLibrary.yml指定的包下
 */
public abstract class ExecutorBase {


    /**
     * 命令的发送者
     */
    protected CommandSender sender;

    /**
     * 参数，注意参数不算上插件名，例如/EasyLibrary help 1的args为{help,1}
     */
    protected String[] args;

    /**
     * 命令的名字，注意参数不算上插件名，例如/EasyLibrary help 1的名字为help
     */
    protected String commandName;

    /**
     * 此构造方法必须有，框架会自动调用，前提是该类的子类必须按规则命名，例如/EasyLibrary help对应的类就是HelpExecutor
     * @param sender
     * @param commandName
     * @param args
     */
    public ExecutorBase(@NotNull CommandSender sender, String commandName, @NotNull String[] args) {
        this.sender = sender;
        this.commandName = commandName;
        this.args = args;
    }

    /**
     * 此方法为框架自动调用，此方法在执行run前，判断玩家是否有权限，权限的节点为插件名.命令名，例如/EasyLibrary help的节点则为EasyLibrary.help
     */
    public void process() {
        String permission = EasyPlugin.instance.getName()+ "." + commandName;
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


    /**
     * 需要自己实现的命令如何处理
     */
    protected abstract void run();

}

