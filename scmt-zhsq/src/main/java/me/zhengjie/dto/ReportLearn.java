/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ReportLearn.java, v 1.0 2020年9月22日 下午8:05:59 yongyan.pu Exp $
 */
@Data
public class ReportLearn implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学习文章数量")
    private Integer           articlesCount;

    @ApiModelProperty("学习完成百分比")
    private Double            complete;

}
