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
 * @version $Id: ReportThreeLessons.java, v 1.0 2020年9月22日 下午5:54:24 yongyan.pu Exp $
 */
@Data
public class ReportThreeLessons implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("党委名称")
    private String            partyCommitteeName;

    //    @ApiModelProperty("1、党小组会 2、支部委员会3、 4、党课")
    //    private Integer           partyCategoryId;
    @ApiModelProperty("党小组会")
    private Integer           partyGroupMeetingNumber;
    @ApiModelProperty("支部委员会")
    private Integer           branchCommitteeNumber;
    @ApiModelProperty("党支部大会")
    private Integer           partyBranchCongressNumber;
    @ApiModelProperty("党课")
    private Integer           partyLectureNumber;

}
