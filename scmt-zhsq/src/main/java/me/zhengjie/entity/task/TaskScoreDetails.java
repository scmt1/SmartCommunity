package me.zhengjie.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务分值明细
 */
@Data
@ApiModel
public class TaskScoreDetails  {

    @ApiModelProperty("数据id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("主单")
    private Long masterId;

    @ApiModelProperty("积分条件")
    private String name;

    @ApiModelProperty("分数")
    private Integer score;

    @TableField(exist = false)
    @ApiModelProperty("是否被选中")
    private Integer status;
}
