package com.spring.simulation.spring;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @create 2023/11/8 20:36
 * @description
 */
@Getter
@Setter
@Builder
@ToString
public class BeanDefinition {

  private Class<?> type;

  private String scope;

  private boolean processor;
}
