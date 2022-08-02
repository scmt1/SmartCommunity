/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.req;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: PartyWorkReq.java, v 1.0 2020年8月12日 下午9:34:19 yongyan.pu Exp $
 */
@Data
public class PartyThreeLessonsSeq implements Serializable {

    /**  */
    private static final long    serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String               id;

    @NotEmpty(message = "topic is empty")
    @ApiModelProperty(value = "议题")
    private String               topic;

    @NotEmpty(message = "initiator is empty")
    @ApiModelProperty(value = "发起人")
    private String               initiator;

    @NotNull(message = "lessonsTime is empty")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "课程时间")
    private Date                 lessonsTime;

    @NotEmpty(message = "lessonsSite is empty")
    @ApiModelProperty(value = "课程地点")
    private String               lessonsSite;

    @NotEmpty(message = "lessonsRequire is empty")
    @ApiModelProperty(value = "课程要求")
    private String               lessonsRequire;

    @NotEmpty(message = "partyCommitteeName is empty")
    @ApiModelProperty(value = "党委名称")
    private String               partyCommitteeName;

    @NotNull(message = "partyCommitteeId is empty")
    @ApiModelProperty(value = "党委id")
    private Long                 partyCommitteeId;

    @NotEmpty(message = "partyCategoryName is empty")
    @ApiModelProperty(value = "分类名称")
    private String               partyCategoryName;

    @NotNull(message = "partyCategoryId is empty")
    @ApiModelProperty(value = "分类id   1、党小组会2、支部委员会3、党支部大会 4、党课")
    private Integer              partyCategoryId;

    @ApiModelProperty(value = "支部名称")
    private String               partyBranchName;

    @NotNull(message = "partyBranchId is empty")
    @ApiModelProperty(value = "支部id")
    private Long                 partyBranchId;

    @ApiModelProperty(value = "会议结果")
    private String               meetingResults;

    @NotNull(message = "lessonsMembers is empty")
    @ApiModelProperty(value = "参会人员")
    private List<me.zhengjie.req.PartyMemberReq> lessonsMembers;

}
