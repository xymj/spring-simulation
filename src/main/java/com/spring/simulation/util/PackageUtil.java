package com.spring.simulation.util;

import com.spring.simulation.clazz.MyClassLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @create 2023/11/8 14:18
 * @description
 */
@Slf4j
public class PackageUtil {

  public static final String FILE_PROTOCOL = "file";
  public static final String JAR_PROTOCOL = "jar";
  public static final String CLASS_FLAG = ".class";
  public static final String POINT_FLAG = ".";
  public static final String SLASH_FLAG = "/";
  public static final String STRING_EMPTY = "";


  /**
   * 获取包下所有类
   * 
   * @param packageName 包名
   * @return 类集合
   */
  public static Set<Class<?>> parseFileClasses(String packageName) {
    Set<Class<?>> classes = new LinkedHashSet<>();
    String packagePath = packageName.replace(POINT_FLAG, SLASH_FLAG);
    boolean recursive = true;
    try {
      Enumeration<URL> resources =
          Thread.currentThread().getContextClassLoader().getResources(packagePath);
      while (resources.hasMoreElements()) {
        URL url = resources.nextElement();
        String protocol = url.getProtocol();
        System.out.println(url.getFile());
        System.out.println(protocol);
        if (FILE_PROTOCOL.equals(protocol)) {
          String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name());
          parseClassesFromFile(packageName, filePath, recursive, classes);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return classes;
  }

  public static Set<Class<?>> parseJarClasses(String jarPath) {
    Set<Class<?>> classes = new LinkedHashSet<>();
    boolean recursive = true;
    parseClassesFromJar(jarPath, recursive, classes);
    return classes;
  }



  /**
   * 获取文件路径下所有类
   * 
   * @param packageName 包名
   * @param filePath 文件路径
   * @param recursive 是否递归
   * @param classes 类集合
   */
  public static void parseClassesFromFile(String packageName, String filePath, boolean recursive,
      Set<Class<?>> classes) {
    File file = new File(filePath);
    if (!file.exists() || !file.isDirectory()) {
      return;
    }

    File[] files = file.listFiles(
        subFile -> (subFile.isDirectory() && recursive) || subFile.getName().endsWith(CLASS_FLAG));

    if (files == null || files.length == 0) {
      return;
    }
    Arrays.stream(files).forEach(childFile -> {
      if (childFile.isDirectory()) {
        parseClassesFromFile(packageName + POINT_FLAG + childFile.getName(),
            childFile.getAbsolutePath(), recursive, classes);
      } else {
        String className = childFile.getName().replace(CLASS_FLAG, STRING_EMPTY);
        try {
          Class<?> aClass = Thread.currentThread().getContextClassLoader()
              .loadClass(packageName + POINT_FLAG + className);
          classes.add(aClass);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
      }
    });

  }

  /**
   * 获取包下所有类
   * 
   * @param recursive 是否递归
   * @param classes 类集合
   */
  public static void parseClassesFromJar(String filePath, boolean recursive,
      Set<Class<?>> classes) {
    File file = new File(filePath);
    if (!file.exists()) {
      return;
    }

    if (file.isDirectory() && recursive) {
      File[] files = file.listFiles();
      Arrays.stream(files)
          .forEach(subFile -> parseClassesFromJar(subFile.getAbsolutePath(), recursive, classes));
    }

    if (file.isFile() && file.getName().endsWith(".jar")) {
      try {
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
          JarEntry jarEntry = jarEntries.nextElement();
          String jarEntryName = jarEntry.getName();
          // System.out.println(jarEntryName);
          if (jarEntryName.endsWith(CLASS_FLAG)) {
            // 方法1 流加载
            InputStream inputStream = jarFile.getInputStream(jarEntry);
            MyClassLoader myClassLoader = new MyClassLoader();
            classes.add(myClassLoader.loadClassFromStream(inputStream));

            // 方法2 loadClass加载
            // jarEntryName = jarEntryName.replace(SLASH_FLAG, POINT_FLAG);
            // System.out.println(jarEntryName);
            // jarEntryName = jarEntryName.replace(CLASS_FLAG, STRING_EMPTY);
            // Class<?> aClass = Thread.currentThread().getContextClassLoader()
            // .loadClass(jarEntryName);
            // classes.add(aClass);
          }
        }
      } catch (Exception e) {
        log.error("ClassNotFoundException error: ", e);
      }
    }
  }


  public static ArrayList<String> getFiles(String path) {
    ArrayList<String> files = new ArrayList<String>();
    File file = new File(path);
    File[] tempList = file.listFiles();

    for (int i = 0; i < tempList.length; i++) {
      if (tempList[i].isFile()) {
        // System.out.println("文 件：" + tempList[i]);
        files.add(tempList[i].toString());
      }
      if (tempList[i].isDirectory()) {
        // System.out.println("文件夹：" + tempList[i]);
        path = tempList[i].toString();
        files.addAll(getFiles(path));
      }
    }
    return files;
  }

  public static void main(String[] args) throws IOException {
    ArrayList<String> files1 = getFiles("src/main/resources/libs");
    System.out.println(files1);

    Set<Class<?>> fileClass = parseFileClasses("com.spring.simulation");
    System.out.println("fileClass:" + fileClass);


    Set<Class<?>> jarClasses = parseJarClasses("src/main/resources/libs");
    System.out.println("jarClasses:" + jarClasses);
  }
}
