package com.spring.simulation.spring.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @create 2023/11/8 20:38
 * @description
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.TYPE)
public @interface Scope {
    String value() default "";
}
