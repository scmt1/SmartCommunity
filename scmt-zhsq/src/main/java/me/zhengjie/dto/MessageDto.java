package me.zhengjie.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
* 消息列表Dto
* */
@Data
public class MessageDto {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("类型")
    private String cood;

    @ApiModelProperty("时间")
    private String createDate;

    @ApiModelProperty("明细id")
    private Integer detailsId;

    @ApiModelProperty("消息类型id")
    private Integer messageType;

    @ApiModelProperty("是否是周期性，1是，2不是，任务模块专有")
    private Integer isCycle;

    @ApiModelProperty("是否即将过期，1是，2不是")
    private Integer willOverdue;

    @ApiModelProperty("用户类型，1是业主，2是员工")
    private Integer userType =2;

    @ApiModelProperty("订单驳回次数" )
    private Integer overrule = 0;

    @ApiModelProperty("紧急程度，0代表空闲，1代表普通，2代表紧急，3代表特急")
    private Integer urgentTypeCount =0;

    @ApiModelProperty("派单人")
    private Integer operateUserId;

    @ApiModelProperty("执行人")
    private String executor;

}
