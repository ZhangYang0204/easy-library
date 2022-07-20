import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.junit.Test;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.listener.PlayerJoin;
import pers.zhangyang.easylibrary.util.ResourceUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ces {



    @Test
    public void t() throws Exception {


        System.out.println(ResourceUtil.getClasssFromJarFile().size());

    }
}
