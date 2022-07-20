package pers.zhangyang.easylibrary.util;


import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ReplaceUtil {

    public static String replaceToRepositoryName(String oldName){

        char[] pluginNameChars= oldName.toCharArray();
        if (pluginNameChars[0]>=65&&pluginNameChars[0]<=90) {
            pluginNameChars[0] += 32;
        }
        boolean end=false;
        while (!end){
            for (int i=0;i< pluginNameChars.length;i++){
                if (pluginNameChars[i]>=65&&pluginNameChars[i]<=90){
                    StringBuilder stringBuilder=new StringBuilder(String.valueOf(pluginNameChars));
                    stringBuilder.replace(i,i+1, String.valueOf((char)( pluginNameChars[i]+32)));
                    stringBuilder.insert(i,"-");
                    pluginNameChars=stringBuilder.toString().toCharArray();
                    break;
                }
            }
            end=true;
            for (char pluginNameChar : pluginNameChars) {
                if (pluginNameChar >= 65 && pluginNameChar <= 90) {
                    end = false;
                    break;
                }
            }
        }
       return String.valueOf(pluginNameChars);

    }

    public static void formatLore(@NotNull ItemStack itemStack, @NotNull String pattern, @Nullable List<String> replaceTo) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta==null){
            return;
        }
        if (itemMeta.getLore() != null) {
            List<String> lore = itemMeta.getLore();
            format(lore, pattern, replaceTo);
            itemMeta.setLore(lore);
        }
        itemStack.setItemMeta(itemMeta);
    }

    public static void replaceDisplayName(@NotNull ItemStack itemStack, @NotNull Map<String, String> replaceMap) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta==null){
            return;
        }
        itemMeta.setDisplayName(replace(itemMeta.getDisplayName(), replaceMap));
        itemStack.setItemMeta(itemMeta);
    }

    public static void replaceLore(@NotNull ItemStack itemStack, @NotNull Map<String, String> replaceMap) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta==null){
            return;
        }
        if (itemMeta.getLore() != null) {
            List<String> lore = itemMeta.getLore();
            replace(lore, replaceMap);
            itemMeta.setLore(lore);
        }
        itemStack.setItemMeta(itemMeta);
    }

    //吧list中pattern的换成replaceTo
    public static void format(@NotNull List<String> list, @NotNull String pattern, @Nullable List<String> replaceTo) {
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            if (s.contains(pattern)) {
                integerList.add(i);

            }
        }
        for (int i = integerList.size() - 1; i >= 0; i--) {
            int ii = integerList.get(i);
            String s = list.get(ii);
            list.remove(ii);
            if (replaceTo != null) {
                for (int j = replaceTo.size() - 1; j >= 0; j--) {
                    list.add(ii, ReplaceUtil.replace(s, Collections.singletonMap(pattern, replaceTo.get(j))));
                }
            }
        }
    }

    @NotNull
    public static String replace(@NotNull String s, @NotNull Map<String, String> rep) {
        for (String key : rep.keySet()) {
            s = s.replace(key, ChatColor.translateAlternateColorCodes('&', rep.get(key)));
        }
        return s;
    }

    public static void replace(@NotNull List<String> s, @NotNull Map<String, String> rep) {
        for (String key : rep.keySet()) {
            for (int i = 0; i < s.size(); i++) {
                s.set(i, s.get(i).replace(key, ChatColor.translateAlternateColorCodes('&', rep.get(key))));
            }
        }
    }

}

