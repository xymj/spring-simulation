package com.spring.simulation.spring.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @create 2023/11/7 19:38
 * @description 扫描注解
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.TYPE)
public @interface ComponentScan {
    String[] value() default {};
}
