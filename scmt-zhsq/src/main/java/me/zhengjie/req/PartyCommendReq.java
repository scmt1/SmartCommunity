/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.req;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: PartyCommendReq.java, v 1.0 2020年8月13日 上午11:25:39 yongyan.pu Exp $
 */
@Data
public class PartyCommendReq implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;

    @NotNull(message = "partyMemberId is empty")
    @ApiModelProperty(value = "党员id")
    private Long              partyMemberId;

    @NotEmpty(message = "title is empty")
    @ApiModelProperty(value = "标题")
    private String            title;

    @NotEmpty(message = "content is empty")
    @ApiModelProperty(value = "内容")
    private String            content;

    @ApiModelProperty(value = "表彰时间")
    private Date              commendDate;

}
