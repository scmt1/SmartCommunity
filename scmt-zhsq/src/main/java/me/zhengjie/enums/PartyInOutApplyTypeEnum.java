package me.zhengjie.enums;

/**
 * 转入转出类型
 * 
 * @author zoutao
 * @version $Id: PartyInOutApplyTypeEnum.java, v 0.1 2020年8月14日 下午9:16:43 zoutao Exp $
 */
public enum PartyInOutApplyTypeEnum {

                                     IN(1, "转入"), OUT(2, "转出");
    /** 编码 */
    private int    code;

    /** 描述 */
    private String desc;

    private PartyInOutApplyTypeEnum(int code, String desc) {
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
