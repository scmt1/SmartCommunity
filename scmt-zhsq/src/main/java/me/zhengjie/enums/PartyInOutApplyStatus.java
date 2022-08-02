package me.zhengjie.enums;

/**
 * 转入转出类型
 * 
 * @author zoutao
 * @version $Id: PartyInOutApplyTypeEnum.java, v 0.1 2020年8月14日 下午9:16:43 zoutao Exp $
 */
public enum PartyInOutApplyStatus {

                                   IN(1, "转入申请"), OUT(2, "转出申请"), ADOPT(3, "通过"), REJECT(4, "驳回")

    ;

    /** 编码 */
    private int    code;

    /** 描述 */
    private String desc;

    private PartyInOutApplyStatus(int code, String desc) {
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
