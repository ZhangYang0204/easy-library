package pers.zhangyang.easylibrary.yaml;

import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.base.YamlBase;

public class MessageYaml extends YamlBase {

    public static final MessageYaml INSTANCE = new MessageYaml();

    protected MessageYaml() {
        super("display/" + SettingYaml.INSTANCE.getDisplay() + "/message.yml");
    }


}
