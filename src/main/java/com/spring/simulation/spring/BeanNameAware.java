package com.spring.simulation.spring;

/**
 * @create 2023/11/9 14:29
 * @description
 */
public interface BeanNameAware {

  /**
   * 设置bean的名称
   *
   * @param beanName
   */
  void setBeanName(String beanName);
}
