package me.zhengjie.enums;

/**
 * 转入转出类型
 * 
 * @author zoutao
 * @version $Id: PartyInOutApplyTypeEnum.java, v 0.1 2020年8月14日 下午9:16:43 zoutao Exp $
 */
public enum EducationTypeEnum {

   xx(1, "小学"), CZ(2, "初中"), GZ(3, "高中"), dz(4,"中专"), DZ(5,"大专"), BK(6,"本科"), ss(7,"硕士"), BS(8,"博士");

    /** 编码 */
    private int    code;

    /** 描述 */
    private String desc;

    private EducationTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(Integer educationId) {
        EducationTypeEnum[] values = EducationTypeEnum.values();
        for (EducationTypeEnum enumType : values) {
            if (enumType.getCode() == educationId) {
                return enumType.getDesc();
            }
        }

        return null;
    }

}
