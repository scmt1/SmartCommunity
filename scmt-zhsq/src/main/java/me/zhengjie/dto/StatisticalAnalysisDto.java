package me.zhengjie.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class StatisticalAnalysisDto implements Serializable {
    /**  */
    private static final long serialVersionUID = -7915345812052802221L;

    /**
     * 统计类型名称
     */
    private String            name;

    /**
     * 统计总数
     */
    private Integer           num;

}
