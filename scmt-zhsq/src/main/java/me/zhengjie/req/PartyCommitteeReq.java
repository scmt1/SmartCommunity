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
public class PartyCommitteeReq implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "name is empty")
    @ApiModelProperty(value = "组织名称")
    private String            name;

    @NotNull(message = "streetId is empty")
    @ApiModelProperty(value = "街道id")
    private Long              streetId;

    @NotEmpty(message = "streetName is empty")
    @ApiModelProperty(value = "街道名称")
    private String            streetName;

    @NotNull(message = "communityId is empty")
    @ApiModelProperty(value = "社区Id")
    private Long              communityId;

    @NotEmpty(message = "communityName is empty")
    @ApiModelProperty(value = "社区名称")
    private String            communityName;

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
