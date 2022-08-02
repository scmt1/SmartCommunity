package me.zhengjie.entity.jpush;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.aop.annotation.NotNull;
import me.zhengjie.entity.BaseEntity;

import java.util.Date;

@Data
public class PushMsg extends BaseEntity {

    @NotNull
    @ApiModelProperty("网格id")
    private String gridId;

    @NotNull
    @ApiModelProperty("应用类型（1：业主，2：物业，3是后台）")
    private Integer appType;

    @ApiModelProperty("用户id（可以是业主或者物业）")
    private String userIds;

    @ApiModelProperty("消息类型（字典）,模块")
    private Integer messageType;

    @NotNull
    @ApiModelProperty("明细id")
    private Long detailsId;

    @ApiModelProperty("消息头")
    private String title;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("参数")
    private String data;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("是否已读 (1：未读；2：已读)")
    private Integer isRead;

}
