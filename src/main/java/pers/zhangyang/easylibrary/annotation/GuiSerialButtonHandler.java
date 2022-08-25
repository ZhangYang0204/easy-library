package pers.zhangyang.easylibrary.annotation;

import org.bukkit.inventory.InventoryHolder;

import java.lang.annotation.*;

/**
 * 有此注解的方法并且类被注册的情况下，当点击的Inventory的InventoryHolder为指定类时，并且槽位为指定值时，并且点击的槽位不为空或空气，会触发此方法内容
 * 与@GuiDiscreteButtonHandler互不干扰
 * 与@EventHandler互不干扰
 * 要在easyLibrary.yml指定的包下的类才有效
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GuiSerialButtonHandler {

    Class<? extends InventoryHolder> guiPage();

    int from();
    boolean closeGui();
    boolean refreshGui();
    int to();


}