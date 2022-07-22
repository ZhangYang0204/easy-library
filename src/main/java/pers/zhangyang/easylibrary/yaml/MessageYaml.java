package pers.zhangyang.easylibrary.yaml;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.base.YamlBase;

public class MessageYaml extends YamlBase {

    public static final MessageYaml INSTANCE = new MessageYaml();

    protected MessageYaml() {
        super("display/" + SettingYaml.INSTANCE.getDisplay() + "/message模板.yml");
    }

    @Nullable
    public String getInput(@NotNull String path) {
        String s = getStringDefault(path);
        if (StringUtils.isBlank(s)) {
            s = backUpConfiguration.getString(path);
        }
        return s;
    }
}
