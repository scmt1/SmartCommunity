package me.zhengjie.entity.gridevents;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.entity.BaseEntity;

/**
  * @Description:    事件记录
  * @Author:         ly
  * @CreateDate:     2019/5/6 13:50
  */
@Data
@ApiModel
public class GridRecord extends BaseEntity {

    @ApiModelProperty("事件id")
    private Long gridEventsId;

    @ApiModelProperty("状态，1是上报事件，2是事件接收，3是事件处理，4是办结审核，5是发起异议,6是事件评价，7异议审核，8分级上报")
    private Integer status;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("加重文字")
    private String keyWords;

    @TableField(exist = false)
    @ApiModelProperty("状态名称")
    private String statusName;




}
