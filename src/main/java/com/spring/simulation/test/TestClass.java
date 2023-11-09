package com.spring.simulation.test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;


public class TestClass {
  public static void main(String[] args) {
    try {
      // 获取当前类的类加载器
      ClassLoader classLoader = TestClass.class.getClassLoader();

      // 使用ClassLoader.getResources()方法获取资源文件的URL列表
      Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources("com");

      // 遍历URL列表，并处理资源文件
      while (resources.hasMoreElements()) {
        URL resourceUrl = resources.nextElement();
        // 在这里进行你的处理...
        System.out.println("Resource URL: " + resourceUrl);
        System.out.println(resourceUrl.getProtocol());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
