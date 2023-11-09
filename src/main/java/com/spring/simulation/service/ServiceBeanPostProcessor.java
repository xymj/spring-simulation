package com.spring.simulation.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.spring.simulation.spring.BeanPostProcessor;
import com.spring.simulation.spring.annotation.Component;

/**
 * @create 2023/11/9 14:45
 * @description
 */
@Component(processor = true)
public class ServiceBeanPostProcessor implements BeanPostProcessor {

  private static final String USER_SERVICE_BEAN_NAME = "userService";
  private static final String ORDER_SERVICE_BEAN_NAME = "orderService";

  @Override
  public Object postProcessBeforeInitialization(String beanName, Object bean) {
    System.out.print("postProcessBeforeInitialization: ");
    if (USER_SERVICE_BEAN_NAME.equals(beanName) && bean instanceof UserService) {
      ((UserService) bean).print();
    }

    if (ORDER_SERVICE_BEAN_NAME.equals(beanName) && bean instanceof OrderService) {
      System.out.println("orderService");
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(String beanName, Object bean) {
    System.out.print("postProcessAfterInitialization: ");
    if (USER_SERVICE_BEAN_NAME.equals(beanName) && bean instanceof UserService) {
      ((UserService) bean).print();
      return Proxy.newProxyInstance(bean.getClass().getClassLoader(),
          bean.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
              System.out.println("aop proxy");
              return method.invoke(bean, args);
            }
          });
    }

    if (ORDER_SERVICE_BEAN_NAME.equals(beanName) && bean instanceof OrderService) {
      System.out.println("orderService");
    }
    return bean;
  }
}
