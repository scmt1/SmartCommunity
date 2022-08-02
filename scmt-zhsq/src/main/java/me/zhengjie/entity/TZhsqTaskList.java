package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TZhsqTaskList对象")
public class TZhsqTaskList implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "任务分类")
    private String taskSort;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "所属社区")
    private String community;

    @ApiModelProperty(value = "所属网格")
    private String grid;

    @ApiModelProperty(value = "所属网格id")
    private String gridId;

    @ApiModelProperty(value = "发起人")
    private String sponsor;

    @ApiModelProperty(value = "任务开始时间")
    private Timestamp taskStartingTime;

    @ApiModelProperty(value = "任务结束时间")
    private Timestamp taskEndTime;

    @ApiModelProperty(value = "执行人")
    private String executor;

    @ApiModelProperty(value = "任务状态 0处理中 1已完成")
    private Integer taskStatus;

    @ApiModelProperty(value = "地图标注")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String position;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "修改人")
    private String updateId;

    @ApiModelProperty(value = "是否删除 0没有删除 1已删除")
    @TableLogic
    private Integer isDelete;


}
