package pers.zhangyang.easylibrary.listener;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.util.ResourceUtil;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@EventListener
public class PlayerClickGuiPage implements Listener {


    @EventHandler
    public void on(InventoryClickEvent event) {
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        int slot = event.getRawSlot();
        ItemStack itemStack = event.getCurrentItem();
        if (inventoryHolder instanceof GuiPage) {
            event.setCancelled(true);
        }
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }
        InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream("easyLibrary模板.yml");
        YamlConfiguration yamlConfiguration=new YamlConfiguration();
        InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        try {
            yamlConfiguration.load(inputStreamReader);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }
        List<Class> classList = ResourceUtil.getClassesFromJarFile(yamlConfiguration.getStringList("guiButtonHandlerPackage"));
        for (Class c : classList) {
            if (Modifier.isInterface(c.getModifiers()) || Modifier.isAbstract(c.getModifiers())) {
                continue;
            }
            Method[] methods = c.getMethods();

            for (Method m : methods) {
                if (m.isAnnotationPresent(GuiDiscreteButtonHandler.class)) {


                    GuiDiscreteButtonHandler guiDiscreteButtonHandler = m.getAnnotation(GuiDiscreteButtonHandler.class);
                    if (!guiDiscreteButtonHandler.guiPage().isInstance(inventoryHolder)) {
                        continue;
                    }
                    List<Integer> integerList = new ArrayList<>();
                    for (int e : guiDiscreteButtonHandler.slot()) {
                        integerList.add(e);
                    }
                    if (!integerList.contains(slot)) {
                        continue;
                    }

                    try {
                        m.setAccessible(true);
                        m.invoke(c.newInstance(), event);
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
                if (m.isAnnotationPresent(GuiSerialButtonHandler.class)) {
                    GuiSerialButtonHandler guiSerialButtonHandler = m.getAnnotation(GuiSerialButtonHandler.class);
                    if (!guiSerialButtonHandler.guiPage().isInstance(inventoryHolder)) {
                        continue;
                    }



                    if (slot>Math.max(guiSerialButtonHandler.from(), guiSerialButtonHandler.to())
                            || slot<Math.min(guiSerialButtonHandler.from(), guiSerialButtonHandler.to())){
                        continue;
                    }

                    try {
                        m.setAccessible(true);
                        m.invoke(c.newInstance(), event);
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }



        }


    }

}
