package pers.zhangyang.easylibrary.base;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.exception.NotApplicableException;
import pers.zhangyang.easylibrary.exception.UnsupportedMinecraftVersionException;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.VersionUtil;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class YamlBase {

    protected YamlConfiguration yamlConfiguration;

    protected String filePath;

    protected YamlConfiguration backUpConfiguration;

    /**
     * @param filePath 在resource下的文件路径
     */
    protected YamlBase(@NotNull String filePath) {
        this.filePath = filePath;
        this.yamlConfiguration = new YamlConfiguration();
        this.backUpConfiguration = new YamlConfiguration();
        init();
    }

    /**
     * 会把对应的文件保存
     */
    public void init() {
        try {
            File file = new File(EasyPlugin.instance.getDataFolder() + "/" + filePath);
            // 如果文件不存在就创建
            if (!file.exists()) {
                File dir = file.getParentFile();
                //先创建目录文件夹
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        throw new IOException();
                    }
                }
                //输出数据
                InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream(filePath);
                if (in == null) {
                    throw new IOException();
                }
                OutputStream out = Files.newOutputStream(file.toPath());
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
            //加载Yaml
            this.yamlConfiguration.load(file);
            InputStream in = YamlBase.class.getClassLoader().getResourceAsStream(filePath);
            if (this.filePath.startsWith("display/")) {
                List<String> argList = new ArrayList<>();
                String[] after = this.filePath.split("/");
                for (String s : after) {
                    if (!s.contains("\\")) {
                        argList.add(s);
                        continue;
                    }
                    argList.addAll(Arrays.asList(s.split("\\\\")));
                }
                StringBuilder defaultFilePath = new StringBuilder();
                for (int i = 0; i < argList.size(); i++) {
                    if (i == 1) {
                        defaultFilePath.append("default/");
                        continue;
                    }
                    if (i != argList.size() - 1) {
                        defaultFilePath.append(argList.get(i)).append("/");
                    } else {
                        defaultFilePath.append(argList.get(i));
                    }
                }

                in = YamlBase.class.getClassLoader().getResourceAsStream(defaultFilePath.toString());
            }
            if (in == null) {
                throw new IOException();
            }
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            this.backUpConfiguration.load(inputStreamReader);

        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修正配置文件内容
     */
    public void correct() {

        //删除多余的
        for (String path : yamlConfiguration.getKeys(true)) {
            if (!backUpConfiguration.getKeys(true).contains(path)) {
                Object ob = yamlConfiguration.get(path);
                yamlConfiguration.set(path, null);
                try {
                    yamlConfiguration.save(EasyPlugin.instance.getDataFolder() + "/" + filePath);
                } catch (IOException e) {
                    yamlConfiguration.set(path, ob);
                    throw new RuntimeException(e);
                }
            }
        }
        //补充缺失的
        for (String pathBase : backUpConfiguration.getKeys(true)) {
            if (!yamlConfiguration.getKeys(true).contains(pathBase)) {
                Object ob = yamlConfiguration.get(pathBase);
                yamlConfiguration.set(pathBase, backUpConfiguration.get(pathBase));
                try {
                    yamlConfiguration.save(EasyPlugin.instance.getDataFolder() + "/" + filePath);
                } catch (IOException e) {
                    yamlConfiguration.set(pathBase, ob);
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @Nullable
    public Boolean getBoolean(@NotNull String path) {
        if (!yamlConfiguration.isBoolean(path)) {
            return null;
        }
        return yamlConfiguration.getBoolean(path);
    }

    @NotNull
    public Boolean getBooleanDefault(@NotNull String path) {
        if (!yamlConfiguration.isBoolean(path)) {
            return backUpConfiguration.getBoolean(path);
        }
        return yamlConfiguration.getBoolean(path);
    }

    @NotNull
    public String getStringDefault(@NotNull String path) {
        if (!yamlConfiguration.isString(path)) {
            return Objects.requireNonNull(backUpConfiguration.getString(path));
        }

        return Objects.requireNonNull(yamlConfiguration.getString(path));
    }

    @Nullable
    public String getString(@NotNull String path) {
        if (!yamlConfiguration.isString(path)) {
            return null;
        }
        return yamlConfiguration.getString(path);
    }

    @Nullable
    public Integer getInteger(@NotNull String path) {
        if (!yamlConfiguration.isInt(path)) {
            return null;
        }
        return yamlConfiguration.getInt(path);
    }

    @NotNull
    public Integer getIntegerDefault(@NotNull String path) {
        if (!yamlConfiguration.isInt(path)) {
            return backUpConfiguration.getInt(path);
        }
        return yamlConfiguration.getInt(path);
    }

    @Nullable
    public Long getLong(@NotNull String path) {
        if (!yamlConfiguration.isLong(path)) {
            return null;
        }
        return yamlConfiguration.getLong(path);
    }

    @NotNull
    public Long getLongDefault(@NotNull String path) {
        if (!yamlConfiguration.isLong(path)) {
            return backUpConfiguration.getLong(path);
        }
        return yamlConfiguration.getLong(path);
    }

    @Nullable
    public Double getDouble(@NotNull String path) {
        if (!yamlConfiguration.isDouble(path)) {
            return null;
        }
        return yamlConfiguration.getDouble(path);
    }

    @NotNull
    public Double getDoubleDefault(@NotNull String path) {
        if (!yamlConfiguration.isDouble(path)) {
            return backUpConfiguration.getDouble(path);
        }
        return yamlConfiguration.getDouble(path);
    }

    @Nullable
    public List<String> getStringList(@NotNull String path) {
        if (!yamlConfiguration.isList(path)) {
            return null;
        }
        return yamlConfiguration.getStringList(path);
    }

    @NotNull
    public List<String> getStringListDefault(@NotNull String path) {
        if (!yamlConfiguration.isList(path)) {
            return backUpConfiguration.getStringList(path);
        }
        return yamlConfiguration.getStringList(path);
    }

    @NotNull
    public Location getLocationDefault(@NotNull String path) {
        Double x = getDoubleDefault(path + ".x");
        Double y = getDoubleDefault(path + ".y");
        Double z = getDoubleDefault(path + ".z");
        Double yaw = getDoubleDefault(path + ".yaw");
        Double pitch = getDoubleDefault(path + ".pitch");
        String worldName = getStringDefault(path + ".worldName");
        World world = Bukkit.getWorld(worldName);
        return new Location(world == null ? Bukkit.getWorld("world") : world, x, y, z, yaw.floatValue(), pitch.floatValue());
    }

    @Nullable
    public Location getLocation(@NotNull String path) {
        Double x = getDouble(path + ".x");
        Double y = getDouble(path + ".y");
        Double z = getDouble(path + ".z");
        Double yaw = getDouble(path + ".yaw");
        Double pitch = getDouble(path + ".pitch");
        String worldName = getString(path + ".worldName");
        if (x == null || y == null || z == null || yaw == null || pitch == null) {
            return null;
        }
        World world = null;
        if (worldName != null) {
            world = Bukkit.getWorld(worldName);
        }

        return new Location(world, x, y, z, yaw.floatValue(), pitch.floatValue());
    }


    @Nullable
    public Integer getNonnegativeInteger(@NotNull String path) {
        Integer var = getInteger(path);
        if (var != null && var < 0) {
            var = backUpConfiguration.getInt(path);
        }
        return var;
    }

    @NotNull
    public Integer getNonnegativeIntegerDefault(@NotNull String path) {
        int var = getIntegerDefault(path);
        if (var < 0) {
            var = backUpConfiguration.getInt(path);
        }
        return var;
    }

    @Nullable
    public Double getNonnegativeDouble(@NotNull String path) {
        Double var = getDouble(path);
        if (var != null && var < 0) {
            var = backUpConfiguration.getDouble(path);
        }
        return var;
    }

    @NotNull
    public Double getNonnegativeDoubleDefault(@NotNull String path) {
        double var = getDoubleDefault(path);
        if (var < 0) {
            var = backUpConfiguration.getDouble(path);
        }
        return var;
    }

    @NotNull
    public ItemStack getButtonDefault(@NotNull String path) {
        String materialName = getStringDefault(path + ".materialName");
        Material material = Material.matchMaterial(materialName);

        if (material == null) {
            materialName = backUpConfiguration.getString(path + ".materialName");
            assert materialName != null;
            material = Material.matchMaterial(materialName);
        }
        assert material != null;
        if (material.equals(Material.AIR)) {
            return new ItemStack(Material.AIR);
        }
        String displayName = getString(path + ".displayName");
        List<String> lore = getStringList(path + ".lore");
        int amount = getIntegerDefault(path + ".amount");
        List<String> itemFlagName = getStringListDefault(path + ".itemFlag");
        List<ItemFlag> itemFlagList = new ArrayList<>();
        Integer customModelData = getInteger(path + ".amount");
        for (String s : itemFlagName) {
            try {
                itemFlagList.add(ItemFlag.valueOf(s));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (VersionUtil.getMinecraftBigVersion() == 1 && VersionUtil
                .getMinecraftMiddleVersion() < 13) {
            try {
                return ItemStackUtil.getItemStack(material, displayName, lore, itemFlagList, amount);
            } catch (NotApplicableException e) {
                return new ItemStack(material);
            }
        } else {
            try {
                return ItemStackUtil.getItemStack(material, displayName, lore, itemFlagList, amount, customModelData);
            } catch (NotApplicableException e) {
                return new ItemStack(material);
            } catch (UnsupportedMinecraftVersionException e) {
                //不会发生的
                e.printStackTrace();
                return null;
            }
        }
    }

    @Nullable
    public ItemStack getButton(@NotNull String path) {
        String materialName = getStringDefault(path + ".materialName");
        Material material = Material.matchMaterial(materialName);

        if (material == null) {
            return null;
        }
        if (material.equals(Material.AIR)) {
            return new ItemStack(Material.AIR);
        }
        String displayName = getString(path + ".displayName");
        List<String> lore = getStringList(path + ".lore");
        int amount = getIntegerDefault(path + ".amount");
        List<String> itemFlagName = getStringListDefault(path + ".itemFlag");
        List<ItemFlag> itemFlagList = new ArrayList<>();
        Integer customModelData = getInteger(path + ".amount");
        for (String s : itemFlagName) {
            try {
                itemFlagList.add(ItemFlag.valueOf(s));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (VersionUtil.getMinecraftBigVersion() == 1 && VersionUtil
                .getMinecraftMiddleVersion() < 13) {
            try {
                return ItemStackUtil.getItemStack(material, displayName, lore, itemFlagList, amount);
            } catch (NotApplicableException e) {
                return new ItemStack(material);
            }
        } else {
            try {
                return ItemStackUtil.getItemStack(material, displayName, lore, itemFlagList, amount, customModelData);
            } catch (NotApplicableException e) {
                return new ItemStack(material);
            } catch (UnsupportedMinecraftVersionException e) {
                //不会发生的
                e.printStackTrace();
                return null;
            }
        }
    }
}
