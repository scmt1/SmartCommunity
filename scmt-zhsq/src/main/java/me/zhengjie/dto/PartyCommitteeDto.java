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
 * @version $Id: PartyCommitteeDto.java, v 1.0 2020年8月12日 下午2:19:37 yongyan.pu Exp $
 */
@Data
public class PartyCommitteeDto implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Id")
    private Long              id;

    @ApiModelProperty(value = "组织名称")
    private String            name;

    @ApiModelProperty(value = "所属辖区ID")
    private Long              jurisdictionId;

    @ApiModelProperty(value = "所属辖区")
    private String            jurisdiction;

    @ApiModelProperty(value = "地址")
    private String            address;

    @ApiModelProperty(value = "手机号码")
    private String            phoneNumber;

    @ApiModelProperty(value = "书记")
    private String            secretary;

    @ApiModelProperty(value = "性别 1、男 2、女")
    private Integer           gender;

}
