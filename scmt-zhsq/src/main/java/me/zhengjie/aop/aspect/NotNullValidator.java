package me.zhengjie.aop.aspect;


import me.zhengjie.aop.annotation.NotNull;
import me.zhengjie.util.BusinessErrorException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 注解逻辑类
 *
 * @author win 10
 */
public class NotNullValidator {

    public static <T> void validator(T t) throws IllegalArgumentException, IllegalAccessException {
        boolean isThrowException = false;
        StringBuilder errorMessage = new StringBuilder();
        Class<? extends Object> cla = t.getClass();
        //利用反射获取类的所有字段包含私有属性
        Field[] fields = cla.getDeclaredFields();
        for (Field f : fields) {
            //判断字段是否有定义NotNull注解
            NotNull notNullAnnotation = f.getAnnotation(NotNull.class);
            if (null != notNullAnnotation) {
                f.setAccessible(true); //设置些属性是可以访问的
                Object val = f.get(t);//得到此属性的值
                boolean flag = notNull(val);
                if (!flag) {
                    //获取注解上的异常信息
                    errorMessage.append(notNullAnnotation.message());
                    String message = "";
                    if (StringUtils.isEmpty(errorMessage.toString())){
                        message =   f.getName()+",不能为空";
                    }else {
                        message =  errorMessage.toString();
                    }
                    exceptionMessage(message);

                }
            }
        }

    }

    /**
     * 判断字段类型以及值是否空
     *
     * @param value
     * @return
     */
    public static boolean notNull(Object value) {
        if (null == value) {
            return false;
        }
        if (value instanceof String && isEmpty(value.toString())) {
            return false;
        }
        if (value instanceof Integer && isEmpty((Integer) value)) {
            return false;
        }
        if (value instanceof List && isEmpty((List<?>) value)) {
            return false;
        }
        return null != value;
    }

    public static boolean isEmpty(String str) {
        return null == str || str.isEmpty();
    }

    public static boolean isEmpty(List<?> list) {
        return null == list || list.isEmpty();
    }

    public static boolean isEmpty(Integer i) {
        return null == i;
    }

    /**
     * 抛出注解对应的异常信息
     *
     * @param errorMessage
     */
    public static void exceptionMessage(String errorMessage) {
        throw new BusinessErrorException(errorMessage);
    }
}