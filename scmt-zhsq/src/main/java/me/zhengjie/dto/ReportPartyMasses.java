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
 * @version $Id: ReportPartyCommittee.java, v 1.0 2020年9月22日 下午5:42:34 yongyan.pu Exp $
 */
@Data
public class ReportPartyMasses implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    //    @ApiModelProperty("党委名称")
    //    private String            partyCommitteeName;

    @ApiModelProperty("社区名称")
    private String            communityName;

    @ApiModelProperty("党群活动次数")
    private Integer           count;
}
