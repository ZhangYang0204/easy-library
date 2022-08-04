package pers.zhangyang.easylibrary.yaml;

import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.base.YamlBase;

import java.io.InputStream;

public class SettingYaml extends YamlBase {

    public static final SettingYaml INSTANCE = new SettingYaml();

    protected SettingYaml() {
        super("setting.yml");

    }


    @NotNull
    public String getDisplay() {
        String display = getStringDefault("setting.display");

        if(SettingYaml.class.getClassLoader().getResource("display/"+display)==null){
            display = backUpConfiguration.getString("setting.display");
        }
        assert display != null;
        return display;
    }

}
