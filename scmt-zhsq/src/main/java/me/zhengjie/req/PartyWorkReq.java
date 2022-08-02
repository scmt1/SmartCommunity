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
 * @version $Id: PartyWorkReq.java, v 1.0 2020年8月12日 下午9:34:19 yongyan.pu Exp $
 */
@Data
public class PartyWorkReq implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "title is empty")
    @ApiModelProperty(value = "标题")
    private String            title;

    @NotNull(message = "partyCommitteeId is empty")
    @ApiModelProperty(value = "党委Id")
    private Long              partyCommitteeId;

    @NotEmpty(message = "partyCommitteeName is empty")
    @ApiModelProperty(value = "党委")
    private String            partyCommitteeName;

    @NotNull(message = "partyCommitteeName is empty")
    @ApiModelProperty(value = "党委支部Id")
    private Long              partBranchId;

    @NotEmpty(message = "partBranchName is empty")
    @ApiModelProperty(value = "党委支部")
    private String            partBranchName;

    @NotNull(message = "partyCategoryId is empty")
    @ApiModelProperty(value = "党员分类")
    private Integer           partyCategoryId;

    @NotEmpty(message = "partyCategoryName is empty")
    @ApiModelProperty(value = "党员分类")
    private String            partyCategoryName;

    @NotEmpty(message = "content is empty")
    @ApiModelProperty(value = "内容")
    private String            content;

}
