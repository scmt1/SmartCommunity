/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author yongyan.pu
 * @version $Id: AppPartyMassessListDto.java, v 1.0 2020年8月13日 下午5:50:55 yongyan.pu Exp $
 */

public class AppPartyMassessListDto implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "ID")
    private Long              id;

    @ApiModelProperty(value = "标题")
    private String            title;

    @ApiModelProperty(value = "活动时间")
    private Date              activityDate;

    @ApiModelProperty(value = "活动状态 1、进行中 2、已结束 3、已参加")
    private Integer           massesStatus;

    @ApiModelProperty("是否已经报名")
    private Boolean           isEnroll;

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMassesStatus() {
        //        if (massesStatus == 1 && getIsEnroll()) {
        //            return 3;
        //        }
        return massesStatus;
    }

    public void setMassesStatus(Integer massesStatus) {
        this.massesStatus = massesStatus;
    }

    public Boolean getIsEnroll() {
        return isEnroll;
    }

    public void setIsEnroll(Boolean isEnroll) {
        this.isEnroll = isEnroll;
    }

}
