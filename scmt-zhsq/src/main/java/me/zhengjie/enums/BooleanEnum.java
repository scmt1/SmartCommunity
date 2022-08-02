package me.zhengjie.enums;

/**
 * boolean 类型枚举，适用于只有两个值（是，否）字段
 * @author zoutao 
 *
 */
public enum BooleanEnum {
                         TRUE(1, "是"), FALSE(0, "否");

    private BooleanEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int    code;
    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
