package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author dengjie
 * @since 2021-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TZhsqBottomTool对象")
public class TZhsqBottomTool implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "上级菜单ID")
    private String pid;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "颜色")
    private String color;

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

    @ApiModelProperty(value = "网格ID")
    private String griddingId;

    @ApiModelProperty(value = "等级")
    private Integer level;

    @ApiModelProperty(value = "是否删除（1是，0否）")
    private Integer isDelete;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "所属街道")
    private String typeId;

    @ApiModelProperty(value = "街道名称")
    private String typeName;

    @ApiModelProperty(value = "网格名称")
    private String griddingName;

    @ApiModelProperty(value = "所属社区")
    private String sourceId;

    @ApiModelProperty(value = "社区名称")
    private String sourceName;

    @ApiModelProperty(value = "连接地址")
    private String url;

    @ApiModelProperty(value = "查询参数")
    private String parameter;

    @ApiModelProperty(value = "子节点")
    @TableField(exist = false)
    private List<TZhsqBottomTool> children;

    @ApiModelProperty(value = "勾选状态")
    @TableField(exist = false)
    private boolean pick;

}
