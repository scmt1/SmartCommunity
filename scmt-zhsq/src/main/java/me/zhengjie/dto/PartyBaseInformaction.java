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
 * @version $Id: PartyBaseInformaction.java, v 1.0 2020年8月13日 下午2:34:36 yongyan.pu Exp $
 */
@Data
public class PartyBaseInformaction implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "地址")
    private String            address;

    @ApiModelProperty(value = "手机号码")
    private String            phoneNumber;

    @ApiModelProperty(value = "书记姓名")
    private String            secretary;

    @ApiModelProperty(value = "性别 1、男 2、女")
    private Integer           gender;

    @ApiModelProperty("党员总数")
    private Integer           totalPartyMember;
    @ApiModelProperty("男党员总数")
    private Integer           totalManPartyMember;
    @ApiModelProperty("女党员总数")
    private Integer           totalWomanPartyMember;
    @ApiModelProperty("35岁以下党员")
    private Integer           yearunder35Total;
    @ApiModelProperty("36-45党员")
    private Integer           year35To45Total;
    @ApiModelProperty("46-54党员")
    private Integer           year46To54Total;
    @ApiModelProperty("55-59党员")
    private Integer           year55To59Total;
    @ApiModelProperty("66以上党员")
    private Integer           yearover60Total;
}
