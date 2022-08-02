package me.zhengjie.entity.gridevents;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.entity.BaseEntity;

/**
  * @Description:    事件处理
  * @Author:         ly
  * @CreateDate:     2019/5/6 13:50
  */
@Data
@ApiModel
public class GridHandle extends BaseEntity {

    @ApiModelProperty("事件id")
    private Long gridEventsId;

    @ApiModelProperty("状态，1是派遣信息，2是处理信息，3是任务审核信息，4是发起异议信息" +
            "，5是事件结束信息，6是评价信息,7是异议审核，8申请异常转派")
    private Integer status;

    @ApiModelProperty("被派遣人id")
    private Long beUserId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("旧音媒体地址")
    private String oldAudioMediaAddress;

    @ApiModelProperty("旧照片-视频地址")
    private String oldMediaAddress;

    @ApiModelProperty("照片-视频地址")
    private String mediaAddress;

    @ApiModelProperty("音媒体地址")
    private String audioMediaAddress;

    @ApiModelProperty("审核状态，1是通过，2是驳回")
    private Integer reviewStatus;

    @ApiModelProperty("审核结果")
    private String reviewContent;

    @ApiModelProperty("评价星级")
    private Integer starRating;

    @ApiModelProperty("订单状态")
    private Integer orderStatus;

}
