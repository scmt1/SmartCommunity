/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.enums;

/**
 * 
 * @author yongyan.pu
 * @version $Id: MassesStatusEnnum.java, v 1.0 2020年8月13日 下午5:21:16 yongyan.pu Exp $
 */
public enum MassesStatusEnum {

                              ONGOING(1, "进行中"), END(2, "结束"), CANCEL(3, "取消");

    /** 编码 */
    private int    code;

    /** 描述 */
    private String desc;

    private MassesStatusEnum(int code, String desc) {
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
