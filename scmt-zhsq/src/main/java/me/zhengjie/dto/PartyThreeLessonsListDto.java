/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: PartyThreeLessonsListDto.java, v 1.0 2020年8月16日 下午2:28:49 yongyan.pu Exp $
 */
@Data
public class PartyThreeLessonsListDto implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long              id;

    @ApiModelProperty(value = "议题")
    private String            topic;

    @ApiModelProperty(value = "课程状态：1 未开始 ，2 已结束")
    private Integer           lessonsStatus;

    @ApiModelProperty("会议时间")
    private Date              lessonsTime;

    @ApiModelProperty("创建时间见")
    private Date              createTime;

    @ApiModelProperty(value = "会议地点")
    private String            lessonsSite;

    @ApiModelProperty(value = "会议要求")
    private String            lessonsRequire;

    public Integer getLessonsStatus() {
        Long current = System.currentTimeMillis();
        Long lessTime = getLessonsTime().getTime();
        return current < lessTime ? 1 : 2;
    }

}
