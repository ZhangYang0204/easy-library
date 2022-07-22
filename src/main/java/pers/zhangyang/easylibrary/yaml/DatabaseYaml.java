package pers.zhangyang.easylibrary.yaml;

import pers.zhangyang.easylibrary.base.YamlBase;

public class DatabaseYaml extends YamlBase {

    public static final DatabaseYaml INSTANCE = new DatabaseYaml();

    protected DatabaseYaml() {
        super("database模板.yml");
    }


}
