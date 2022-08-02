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
 * @version $Id: ReportPartyMemberAageStatistics.java, v 1.0 2020年9月22日 下午5:20:02 yongyan.pu Exp $
 */

@Data
public class ReportPartyMemberAageStatistics implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("党员总数")
    private Integer           totalPartyMember;
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
