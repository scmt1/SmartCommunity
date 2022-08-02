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
 * @since 2020-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BasicGridPersonPoint对象")
public class BasicGridPersonPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "网格长编号")
    private String gridPersonId;

    @ApiModelProperty(value = "网格长姓名")
    private String gridPersonName;

    @ApiModelProperty(value = "坐标点")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String position;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否删除 1是 0 否")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "修改人")
    private String updateId;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "删除人")
    private String deleteId;

    @ApiModelProperty(value = "删除时间")
    private Timestamp deleteTime;


}
