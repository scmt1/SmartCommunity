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
 * @version $Id: PartyWorkDetail.java, v 1.0 2020年8月13日 下午11:02:11 yongyan.pu Exp $
 */
@Data
public class PartyWorkDetail implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long              id;
    @ApiModelProperty(value = "标题")
    private String            title;
    @ApiModelProperty("党务内容")
    private String            content;

    @ApiModelProperty(value = "创建时间")
    private Date              createTime;

}
