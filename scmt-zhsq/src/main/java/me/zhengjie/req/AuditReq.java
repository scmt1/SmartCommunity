/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: AuditReq.java, v 1.0 2020年8月16日 下午2:56:45 yongyan.pu Exp $
 */
@Data
public class AuditReq implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "党委Id")
    private Long              partyCommitteeId;

    @ApiModelProperty(value = "党委名称")
    private String            partyCommitteeName;

    @ApiModelProperty(value = "党支部")
    private Long              partyBranchId;

    @ApiModelProperty(value = "党支部名称")
    private String            partyBranchName;

}
