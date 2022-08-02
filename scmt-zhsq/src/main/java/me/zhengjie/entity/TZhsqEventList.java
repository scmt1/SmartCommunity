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
@ApiModel(value="TZhsqEventList对象")
public class TZhsqEventList implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "事件分类")
    private String eventClassification;

    @ApiModelProperty(value = "事件名称")
    private String eventName;

    @ApiModelProperty(value = "所属社区")
    private String community;

    @ApiModelProperty(value = "所属网格")
    private String grid;

    @ApiModelProperty(value = "所属网格id")
    private String gridId;

    @ApiModelProperty(value = "发起人")
    private String sponsor;

    @ApiModelProperty(value = "事件开始时间")
    private Timestamp eventStartingTime;

    @ApiModelProperty(value = "事件结束时间")
    private Timestamp eventEndTime;

    @ApiModelProperty(value = "执行人")
    private String executor;

    @ApiModelProperty(value = "事件状态 0处理中 1已完成")
    private Integer eventStatus;

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

    @ApiModelProperty(value = "所属街道")
    private String street;

    @ApiModelProperty(value = "所属街道id")
    private String streetId;


    @ApiModelProperty(value = "所属社区id")
    private String communityId;
}
