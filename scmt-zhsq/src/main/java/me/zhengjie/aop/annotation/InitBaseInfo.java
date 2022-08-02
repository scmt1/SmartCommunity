package me.zhengjie.aop.annotation;


import me.zhengjie.aop.type.InitBaseType;

import java.lang.annotation.*;

/**
 * 初始化基础信息
 * @author ljj
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InitBaseInfo {

    InitBaseType type();

}
