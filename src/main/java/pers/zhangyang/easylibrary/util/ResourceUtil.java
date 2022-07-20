package pers.zhangyang.easylibrary.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easylibrary.exception.FailureDeleteFileException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourceUtil {


    public static List<Class> getClasssFromJarFile(List<String> packageList) {
        String jarPaht = ResourceUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        List<Class> clazzs = new ArrayList<>();

        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPaht);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        List<JarEntry> jarEntryList = new ArrayList<>();

        Enumeration<JarEntry> ee = jarFile.entries();

        while (ee.hasMoreElements()) {
            JarEntry entry =ee.nextElement();
            // 过滤我们出满足我们需求的东西
            for (String s:packageList) {
                if (entry.getName().startsWith(s) && entry.getName().endsWith(".class")) {
                    jarEntryList.add(entry);
                }
            }
        }
        for (JarEntry entry : jarEntryList) {
            String className = entry.getName().replace('/', '.');
            className = className.substring(0, className.length() - 6);
            // 也可以采用如下方式把类加载成一个输入流
            // InputStream in = jarFile.getInputStream(entry);
            try {
                clazzs.add(ResourceUtil.class.getClassLoader().loadClass(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazzs;
    }
    public static void deleteFile(@NotNull File file) throws FailureDeleteFileException {
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File deleteFile : files) {
            if (deleteFile.isDirectory()) {
                //判断如果是文件夹，则递归删除下面的文件后再删除该文件夹
                deleteFile(deleteFile);
            } else {
                //文件直接删除
                deleteFile(deleteFile);
            }
        }
        if (!file.delete()) {
            throw new FailureDeleteFileException();
        }
    }

    @Nullable
    public static String readFirstLine(URL url) throws IOException {
        if (url == null) {
            throw new NullPointerException();
        }
        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.readLine();
    }

}
