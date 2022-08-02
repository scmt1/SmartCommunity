/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: MyDuesDto.java, v 1.0 2020年8月13日 下午11:52:44 yongyan.pu Exp $
 */
@Data
public class MyDuesDto implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "缴纳日期")
    private Date              payDate;

    @ApiModelProperty(value = "缴纳金额")
    private Integer           amount;

}
