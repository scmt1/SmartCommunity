/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author zoutao
 * @version $Id: PartyThreeLessonsDto.java, v 0.1 2020年8月12日 下午11:06:39 zoutao Exp $
 */

@Data
@ApiModel(value = "PartyThreeLessons对象", description = "三学一课")
public class PartyThreeLessonsDto implements Serializable {

    /**  */
    private static final long      serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long                   id;

    @ApiModelProperty(value = "议题")
    private String                 topic;

    @ApiModelProperty(value = "发起人")
    private String                 initiator;

    @ApiModelProperty(value = "课程时间")
    private Date                   lessonsTime;

    @ApiModelProperty(value = "课程地点")
    private String                 lessonsSite;

    @ApiModelProperty(value = "课程要求")
    private String                 lessonsRequire;

    @ApiModelProperty(value = "党委名称")
    private String                 partyCommitteeName;

    @ApiModelProperty(value = "党委id")
    private Long                   partyCommitteeId;

    @ApiModelProperty(value = "分类id")
    private Integer                partyCategoryId;

    @ApiModelProperty(value = "支部名称")
    private String                 partyBranchName;

    @ApiModelProperty(value = "支部id")
    private Long                   partyBranchId;

    @ApiModelProperty(value = "会议结果")
    private String                 meetingResults;

    @ApiModelProperty(value = "分类名称")
    private String                 partyCategoryName;

    @ApiModelProperty(value = "听课人员对象")
    private List<LessonsMemberDto> lessonsMembers;

}
