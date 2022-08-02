package me.zhengjie.aop.annotation;

import java.lang.annotation.*;

/**
 * 初始化基础信息
 * @author ljj
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String value() default "";
}
