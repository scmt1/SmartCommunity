/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package me.zhengjie.enums;

/**
 *  学习资料来源枚举类型
 * @author zoutao
 * @version $Id: PartyLearningMaterialsSourceEnum.java, v 0.1 2020年8月13日 下午5:15:08 zoutao Exp $
 */
public enum PartyLearningMaterialsSourceEnum {
    
    
    
    LXYZ(1,"两学一做"),
    MFXS(2,"模范宣塑")
    ;
    private PartyLearningMaterialsSourceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    
    private int code;
    private String desc;
    public int getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
