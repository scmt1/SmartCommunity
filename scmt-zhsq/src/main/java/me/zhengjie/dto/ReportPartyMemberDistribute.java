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
 * @version $Id: ReportPartyMemberDistribute.java, v 1.0 2020年9月22日 下午6:18:13 yongyan.pu Exp $
 */

@Data
public class ReportPartyMemberDistribute implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;
    //    @ApiModelProperty("党委名称")
    //    private String            partyCommitteeName;

    @ApiModelProperty("社区名称")
    private String            communityName;

    @ApiModelProperty("党员数量")
    private Integer           count;

}
