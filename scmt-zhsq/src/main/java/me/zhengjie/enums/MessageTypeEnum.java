/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.enums;

/**
 * 
 * @author yongyan.pu
 * @version $Id: MessageTypeEnum.java, v 1.0 2020年8月14日 下午11:24:25 yongyan.pu Exp $
 */
public enum MessageTypeEnum {

                             PARTYWORK(1, "党务"), PARTYMASSES(2, "党群");

    /** 编码 */
    private int    code;

    /** 描述 */
    private String desc;

    private MessageTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
