package me.zhengjie.aop.type;

/**
 * 区分查重类型
 * @author ljj
 */
public enum InitBaseType {

    /**
     * 添加
     */
    ADD(1),
    /**
     * 更新
     */
    UPDATE(2);

    /**
     * 类型对应的数字
     */
    private Integer typeNum;


    public Integer getTypeNum() {
        return typeNum;
    }

    public void setTypeNum(Integer typeNum) {
        this.typeNum = typeNum;
    }

    InitBaseType(Integer typeNum) {
        this.typeNum = typeNum;
    }

}
