package com.spring.simulation.spring;

/**
 * @create 2023/11/9 14:34
 * @description
 */
public interface InitializingBean {
  /**
   * 在容器初始化完成后调用该方法
   *
   * @throws Exception
   */
  void afterPropertiesSet();
}
