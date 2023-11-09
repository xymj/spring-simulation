package com.spring.simulation.spring.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @create 2023/11/9 14:07
 * @description
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.FIELD)
public @interface Autowired {
    String name() default "";
}
