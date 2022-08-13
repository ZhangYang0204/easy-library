package pers.zhangyang.easylibrary.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.exception.NotApplicableException;
import pers.zhangyang.easylibrary.exception.UnsupportedMinecraftVersionException;

import java.util.List;

public class ItemStackUtil {


    /**
     *
     * @return 玩家头颅物品
     */
    @NotNull
    public static ItemStack getPlayerSkullItem() {
        if (VersionUtil.getMinecraftBigVersion() == 1 && VersionUtil.getMinecraftMiddleVersion() < 13) {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
        } else {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        }
    }

    /**
     *把itemStack的name  lore  customModelData  移植到target里  把itemStack有的itemFlag直接添加到target里
     */
    public static void apply(@NotNull ItemStack itemStack, @NotNull ItemStack target) throws NotApplicableException {
        ItemMeta itemMeta = itemStack.getItemMeta();
        ItemMeta targetMeta = target.getItemMeta();
        if (targetMeta == null) {
            throw new NotApplicableException();
        }
        if (itemMeta == null) {
            return;
        }
        targetMeta.setDisplayName(itemMeta.getDisplayName());
        targetMeta.setLore(itemMeta.getLore());
        for (ItemFlag i : itemMeta.getItemFlags()) {
            targetMeta.addItemFlags(i);
        }
        if (VersionUtil.getMinecraftBigVersion() == 1 && VersionUtil
                .getMinecraftMiddleVersion() >= 13 && itemMeta.hasCustomModelData()) {
            targetMeta.setCustomModelData(itemMeta.getCustomModelData());
        }
        target.setItemMeta(targetMeta);
    }


    @NotNull
    public static ItemStack getItemStack(@NotNull Material material, @Nullable String displayName,
                                         @Nullable List<String> lore, @Nullable List<ItemFlag> flagList,
                                         int amount) throws NotApplicableException {

        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            throw new NotApplicableException();
        }
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
            }
            itemMeta.setLore(lore);
        }
        if (displayName != null) {
            displayName = ChatColor.translateAlternateColorCodes('&', displayName);
            itemMeta.setDisplayName(displayName);
        }
        if (flagList != null) {
            for (ItemFlag itemFlag : flagList) {
                itemMeta.addItemFlags(itemFlag);
            }
        }
        if (!itemStack.setItemMeta(itemMeta)) {
            throw new IllegalArgumentException();
        }
        return itemStack;

    }

    @NotNull
    public static ItemStack getItemStack(@NotNull Material material, @Nullable String displayName,
                                         @Nullable List<String> lore, @Nullable List<ItemFlag> flagList,
                                         int amount, @Nullable Integer customModelData) throws NotApplicableException, UnsupportedMinecraftVersionException {
        if (VersionUtil.getMinecraftBigVersion() == 1 && VersionUtil
                .getMinecraftMiddleVersion() < 13) {
            throw new UnsupportedMinecraftVersionException();
        }
        ItemStack itemStack = getItemStack(material, displayName, lore, flagList, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setCustomModelData(customModelData);
        assert itemStack.setItemMeta(itemMeta);
        return itemStack;

    }

    @NotNull
    public static String itemStackSerialize(@NotNull ItemStack itemStack) {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("item", itemStack);
        return yml.saveToString();
    }

    @NotNull
    public static ItemStack itemStackDeserialize(@NotNull String str) {
        YamlConfiguration yml = new YamlConfiguration();
        try {
            yml.loadFromString(str);
        } catch (InvalidConfigurationException e) {
            throw new IllegalArgumentException();
        }
        Object obj = yml.get("item");
        if (obj == null) {
            throw new IllegalArgumentException();
        }
        return (ItemStack) obj;
    }
}
