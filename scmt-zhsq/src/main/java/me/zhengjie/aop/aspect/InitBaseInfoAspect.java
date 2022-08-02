package me.zhengjie.aop.aspect;

import com.alibaba.fastjson.JSONObject;

import lombok.AllArgsConstructor;
import me.zhengjie.aop.annotation.InitBaseInfo;
import me.zhengjie.aop.type.InitBaseType;
import me.zhengjie.entity.BaseEntity;
import me.zhengjie.common.utils.SecurityUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自动设置基础属性值
 *
 * @author ljj
 */
@Aspect
@Component
@AllArgsConstructor
public class InitBaseInfoAspect {

    private final static String CREATE_TIME_FIELD = "createTime";

    private final static String CREATE_USER_FIELD = "createUser";

    private final static String UPDATE_TIME_FIELD = "updateTime";

    private final static String UPDATE_USER_FIELD = "updateUser";

    private final static String IS_DELETED_FIELD = "isDeleted";

    private final SecurityUtil securityUtil;

    @Pointcut("@annotation(me.zhengjie.aop.annotation.InitBaseInfo)")
    public void annotationPointCut() {}

    @Before("annotationPointCut()")
    public void before(JoinPoint joinPoint) throws IllegalAccessException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        InitBaseInfo initBaseInfo = method.getAnnotation(InitBaseInfo.class);
        // 获取更新或者新增
        InitBaseType type = initBaseInfo.type();
        Object[] args = joinPoint.getArgs();
        for (Object parameter : args) {
            Class<?> model = parameter.getClass();
            // 帅选出实体类
            if (null != model.getSuperclass() && model.getSuperclass().equals(BaseEntity.class) ){
                Date date = new Date();
                Long userId = null;
                // 获取当前用户信息
//                JSONObject userInfo = userInfoService.loadCurrentInfo();
//                if (userInfo.containsKey("id")){
//                    userId = userInfo.getLong("id");
//                }
                userId = securityUtil.getCurrUser().getId();
                // 获取BaseEntity类
                Class<?> baseModel = model.getSuperclass();
                Field[] declaredFields = baseModel.getDeclaredFields();
                // 筛选需要初始化的字段
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    if (CREATE_TIME_FIELD.equals(field.getName()) && type.getTypeNum() == 1){
                        field.set(parameter,date);
                    }
                    if (CREATE_USER_FIELD.equals(field.getName()) && type.getTypeNum() == 1 && null != userId){
                        field.set(parameter,userId);
                    }
                    if (IS_DELETED_FIELD.equals(field.getName()) && type.getTypeNum() == 1){
                        field.set(parameter,0);
                    }
                    if (UPDATE_TIME_FIELD.equals(field.getName()) && type.getTypeNum() == 2){
                        field.set(parameter,date);
                    }
                    if (UPDATE_USER_FIELD.equals(field.getName()) && type.getTypeNum() == 2 && null != userId){
                        field.set(parameter,userId);
                    }
                }
            }
        }
    }

}
