/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.req;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: PartyDuesReq.java, v 1.0 2020年8月13日 上午11:52:44 yongyan.pu Exp $
 */
@Data
public class PartyDuesReq implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @NotNull(message = "partyMemberId is empty")
    @ApiModelProperty(value = "党员id")
    private Long              partyMemberId;

    @NotNull(message = "payDate is empty")
    @ApiModelProperty(value = "缴纳日期")
    private Date              payDate;

    @NotNull(message = "amount is empty")
    @ApiModelProperty(value = "缴纳金额")
    private Integer           amount;

    //    @ApiModelProperty(value = "季度")
    //    private Integer           quarter;

    @NotNull(message = "remark is empty")
    @ApiModelProperty(value = "备注")
    private String            remark;
}
