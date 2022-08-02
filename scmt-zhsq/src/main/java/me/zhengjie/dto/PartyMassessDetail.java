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
 * @version $Id: PartyMassessDetail.java, v 1.0 2020年8月13日 下午10:07:52 yongyan.pu Exp $
 */
@Data
public class PartyMassessDetail implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "党群Id")
    private Long              id;

    @ApiModelProperty(value = "标题")
    private String            title;

    @ApiModelProperty(value = "活动时间")
    private Date              activityDate;

    @ApiModelProperty("內容")
    private String            content;

}
