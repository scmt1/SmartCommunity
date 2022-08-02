/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.req;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: PartyCommitteeReq.java, v 1.0 2020年8月12日 下午1:52:19 yongyan.pu Exp $
 */
@Data
public class PartyBranchReq implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "name is empty")
    @ApiModelProperty(value = "组织名称")
    private String            name;

    @NotNull(message = "partyCommitteeId is empty")
    @ApiModelProperty(value = "所属党委id")
    private Long              partyCommitteeId;

    @NotEmpty(message = "partyCommitteeName is empty")
    @ApiModelProperty(value = "所属党委")
    private String            partyCommitteeName;

    //    @NotNull(message = "jurisdictionId is empty")
    //    @ApiModelProperty(value = "所属辖区id")
    //    private Long              jurisdictionId;
    //
    //    @NotEmpty(message = "jurisdictionName is empty")
    //    @ApiModelProperty(value = "所属辖区")
    //    private String            jurisdictionName;

    @NotEmpty(message = "address is empty")
    @ApiModelProperty(value = "地址")
    private String            address;

    @NotEmpty(message = "phoneNumber is empty")
    @ApiModelProperty(value = "手机号码")
    private String            phoneNumber;

    @NotEmpty(message = "secretary is empty")
    @ApiModelProperty(value = "书记")
    private String            secretary;

    @NotNull(message = "gender is empty")
    @ApiModelProperty(value = "性别 1、男 2、女")
    private Integer           gender;

}
