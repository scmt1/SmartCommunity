/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReportPartyOrganization implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("社区名称")
    private String            communityName;

    @ApiModelProperty("组织数量")
    private String            count;
}
