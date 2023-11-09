package com.spring.simulation.spring;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.spring.simulation.spring.annotation.Autowired;
import com.spring.simulation.spring.annotation.Component;
import com.spring.simulation.spring.annotation.ComponentScan;
import com.spring.simulation.spring.annotation.Scope;
import com.spring.simulation.util.PackageUtil;

/**
 * @create 2023/11/7 19:35
 * @description 启动类
 */
public class ApplicationContext {

  private static final String SINGLETON = "singleton";
  private static final String PROTOTYPE = "prototype";

  private Class configClass;
  private final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap =
      new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, Object> singletonBeanMap = new ConcurrentHashMap<>();

  private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

  public ApplicationContext(Class configClass) {
    this.configClass = configClass;

    if (!configClass.isAnnotationPresent(ComponentScan.class)) {
      return;
    }
    ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
    String[] value = componentScan.value();
    List<BeanDefinition> beanDefinitions =
        Arrays.stream(value).flatMap(path -> PackageUtil.parseFileClasses(path).stream())
            .filter(clazz -> clazz.isAnnotationPresent(Component.class)).map(clazz -> {
              String scope = SINGLETON;
              if (clazz.isAnnotationPresent(Scope.class)) {
                String scopeVal = clazz.getAnnotation(Scope.class).value();
                if (StringUtils.isNotBlank(scopeVal) && PROTOTYPE.equals(scopeVal)) {
                  scope = PROTOTYPE;
                }
              }
              return BeanDefinition.builder().type(clazz).scope(scope)
                  .processor(clazz.getAnnotation(Component.class).processor()).build();
            }).collect(Collectors.toList());
    beanDefinitions.forEach(System.out::println);

    beanDefinitions.forEach(beanDefinition -> {
      Class<?> clazz = beanDefinition.getType();
      String beanName = clazz.getAnnotation(Component.class).value();
      if (StringUtils.isBlank(beanName)) {
        beanName = Introspector.decapitalize(clazz.getSimpleName());
      }
      beanDefinitionMap.put(beanName, beanDefinition);
    });
    System.out.println("beanDefinitionMap: " + beanDefinitionMap);


    beanDefinitionMap.forEach((beanName, beanDefinition) -> {
      if (beanDefinition.isProcessor()) {
        beanPostProcessors.add((BeanPostProcessor) createBean(beanName, beanDefinition));
      }
    });
    System.out.println("beanPostProcessors: " + beanPostProcessors);

    beanDefinitionMap.forEach((beanName, beanDefinition) -> {
      String scope = beanDefinition.getScope();
      if (SINGLETON.equals(scope)) {
        singletonBeanMap.put(beanName, createBean(beanName, beanDefinition));
      }
    });
    System.out.println("singletonBeanMap: " + singletonBeanMap);
  }

  private Object createBean(String beanName, BeanDefinition beanDefinition) {
    Class<?> clazz = beanDefinition.getType();
    try {
      // 通过类的无参构造方法生成一个实例对象
      Object obj = clazz.getConstructor().newInstance();
      // 依赖注入
      for (Field field : obj.getClass().getDeclaredFields()) {
        if (field.isAnnotationPresent(Autowired.class)) {
          field.setAccessible(true);
          String name = field.getAnnotation(Autowired.class).name();
          String fieldName = field.getName();
          field.set(obj, getBean(StringUtils.isBlank(name) ? fieldName : name));
        }
      }

      // Aware回调
      if (obj instanceof BeanNameAware) {
        ((BeanNameAware) obj).setBeanName(beanName);
      }

      // 初始化前
      for (BeanPostProcessor processor : beanPostProcessors) {
        obj = processor.postProcessBeforeInitialization(beanName, obj);
      }

      // 初始化
      if (obj instanceof InitializingBean) {
        ((InitializingBean) obj).afterPropertiesSet();
      }

      // 初始化后
      for (BeanPostProcessor processor : beanPostProcessors) {
        obj = processor.postProcessAfterInitialization(beanName, obj);
      }
      return obj;
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }


  public Object getBean(String beanName) {
    BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
    if (null == beanDefinition) {
      throw new NullPointerException(beanName + " for beanDefinition is null");
    }
    String scope = beanDefinition.getScope();
    if (SINGLETON.equals(scope)) {
      Object singletonBean = singletonBeanMap.get(beanName);
      if (null != singletonBean) {
        return singletonBean;
      }
      singletonBean = createBean(beanName, beanDefinition);
      singletonBeanMap.put(beanName, singletonBean);
      return singletonBean;
    }
    return createBean(beanName, beanDefinition);
  }
}
