package pers.zhangyang.easylibrary.listener;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.MultipleGuiPageBase;
import pers.zhangyang.easylibrary.exception.NotExistNextPageException;
import pers.zhangyang.easylibrary.exception.NotExistPreviousPageException;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.ResourceUtil;
import pers.zhangyang.easylibrary.yaml.DatabaseYaml;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PlayerClickGuiPage implements Listener {


    @EventHandler
    public void on(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        int slot = event.getRawSlot();
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return;
        }


        //点击到GuiPage子类的操作
        if (inventoryHolder instanceof GuiPage) {
            event.setCancelled(true);
            //返回
            if (inventoryHolder instanceof BackAble) {
                BackAble backAble = (BackAble) inventoryHolder;
                if (backAble.getBackSlot() == slot) {
                    backAble.back();
                }
            }
            //下一页
            if (inventoryHolder instanceof MultipleGuiPageBase) {
                MultipleGuiPageBase multipleGuiPageBase = (MultipleGuiPageBase) inventoryHolder;
                if (multipleGuiPageBase.getNextPageSlot() == slot) {
                    try {
                        multipleGuiPageBase.nextPage();
                    } catch (NotExistNextPageException e) {
                        MessageUtil.sendMessageTo(event.getWhoClicked(), MessageYaml.INSTANCE.getStringList("message.chat.notExistNextPage"));
                    }
                }
            }
            //上一页
            if (inventoryHolder instanceof MultipleGuiPageBase) {
                MultipleGuiPageBase multipleGuiPageBase = (MultipleGuiPageBase) inventoryHolder;
                if (multipleGuiPageBase.getPreviousPageSlot() == slot) {
                    try {
                        multipleGuiPageBase.previousPage();
                    } catch (NotExistPreviousPageException e) {
                        MessageUtil.sendMessageTo(event.getWhoClicked(), MessageYaml.INSTANCE.getStringList("message.chat.notExistPreviousPage"));
                    }
                }
            }
        }


        //按钮被点击注解操作
        InputStream in = DatabaseYaml.class.getClassLoader().getResourceAsStream("easyLibrary.yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        assert in != null;
        InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        try {
            yamlConfiguration.load(inputStreamReader);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        List<Class> classList;
        try {
            classList = ResourceUtil.getClassesFromJarFile(yamlConfiguration.getStringList("guiButtonHandlerPackage"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        for (Class c : classList) {
            if (!c.isAnnotationPresent(EventListener.class)) {
                continue;
            }
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

                        if (inventoryHolder instanceof GuiPage){
                            GuiPage guiPage= (GuiPage) inventoryHolder;
                            guiPage.refresh();
                        }


                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    }


                }
                if (m.isAnnotationPresent(GuiSerialButtonHandler.class)) {
                    GuiSerialButtonHandler guiSerialButtonHandler = m.getAnnotation(GuiSerialButtonHandler.class);
                    if (!guiSerialButtonHandler.guiPage().isInstance(inventoryHolder)) {
                        continue;
                    }


                    if (slot > Math.max(guiSerialButtonHandler.from(), guiSerialButtonHandler.to())
                            || slot < Math.min(guiSerialButtonHandler.from(), guiSerialButtonHandler.to())) {
                        continue;
                    }

                    try {
                        m.setAccessible(true);
                        m.invoke(c.newInstance(), event);
                        if (inventoryHolder instanceof GuiPage){
                            GuiPage guiPage= (GuiPage) inventoryHolder;
                            guiPage.refresh();
                        }

                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    }

                }
            }


        }

    }

}
