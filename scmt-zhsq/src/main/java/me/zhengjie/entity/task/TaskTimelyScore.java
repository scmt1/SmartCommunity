package me.zhengjie.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 及时率分值（与小区一对一）
 * @author ljj
 */
@Data
@ApiModel
public class TaskTimelyScore  {

    @ApiModelProperty("数据id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("最早抢单有效时间")
    private Integer grabOrderFirst;

    @ApiModelProperty("最高抢单时间内得分")
    private Integer grabOrderFirstScore;

    @ApiModelProperty("最早抢单有效时间")
    private Integer grabOrderFirstStatus;

    @ApiModelProperty("最早抢单的标记号，默认为1")
    private Integer grabOrderFirstNum;

    @ApiModelProperty("最迟抢单有效时间")
    private Integer grabOrderLast;

    @ApiModelProperty("最迟抢单时间内得分")
    private Integer grabOrderLastScore;

    @ApiModelProperty("最迟抢单有效时间")
    private Integer grabOrderLastStatus;

    @ApiModelProperty("最迟抢单的标记号，默认为2")
    private Integer grabOrderLastNum;

    @ApiModelProperty("完成单据时间")
    private Integer completeOrder;

    @ApiModelProperty("完成单据得分")
    private Integer completeOrderScore;

    @ApiModelProperty("完成单据得分状态")
    private Integer completeOrderStatus;

    @ApiModelProperty("完成单据得分的标记号，默认为3")
    private Integer completeOrderNum;
}
