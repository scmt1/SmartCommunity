package me.zhengjie.util;

/**
 * 业务错误异常类（懒人版）
 * @author dengjie
 */
public class BusinessErrorException extends RuntimeException{

    public BusinessErrorException(String msg){
        super(msg);
    }
}
