package com.spring.simulation.spring;

/**
 * @create 2023/11/9 14:39
 * @description
 */
public interface BeanPostProcessor {
  /**
   * 在bean初始化之前调用该方法
   *
   * @param beanName
   * @param bean
   */
  Object postProcessBeforeInitialization(String beanName, Object bean);

  /**
   * 在bean初始化之后调用该方法
   *
   * @param beanName
   * @param bean
   */
  Object postProcessAfterInitialization(String beanName, Object bean);
}
