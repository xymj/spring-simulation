package com.spring.simulation.service;

import com.spring.simulation.spring.BeanNameAware;
import com.spring.simulation.spring.InitializingBean;
import com.spring.simulation.spring.annotation.Autowired;
import com.spring.simulation.spring.annotation.Component;
import com.spring.simulation.spring.annotation.Scope;

/**
 * @create 2023/11/7 19:36
 * @description
 */
@Component(value = "userService")
@Scope
public class UserService implements UserServiceInterface, BeanNameAware, InitializingBean {

  @Autowired
  private OrderService orderService;

  private String beanName;

  private String name;

  @Override
  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  @Override
  public void afterPropertiesSet() {
    System.out.println("initializingBean");
    this.name = "initialized properties";
  }

  @Override
  public void print() {
    System.out
        .println("beanName: " + beanName + ", orderService: " + orderService + ", name: " + name);
  }

}
