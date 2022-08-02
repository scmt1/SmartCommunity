package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Timestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@ApiModel(value="TPopulationlabel对象")
public class TPopulationlabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "父id")
    private String pid;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    private String updateId;

    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

    @ApiModelProperty(value = "删除人")
    private String deleteId;

    @ApiModelProperty(value = "是否删除 0没有删除 1已删除")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "是否显示标签 0隐藏 1显示")
    private String isLabel;

    @TableField(exist = false)
    private String parentName;

    @TableField(exist = false)
    private String label;

    @ApiModelProperty(value = "子节点")
    @TableField(exist = false)
    private List<TPopulationlabel> children;

}
