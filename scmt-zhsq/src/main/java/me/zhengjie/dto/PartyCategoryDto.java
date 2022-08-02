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
 * @version $Id: PartyCategoryDto.java, v 1.0 2020年8月12日 下午9:45:15 yongyan.pu Exp $
 */

@Data
public class PartyCategoryDto implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分类Id")
    private Integer           partyCategoryId;

    @ApiModelProperty("分类名称")
    private String            partyCategoryName;

}
