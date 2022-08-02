package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
@ApiModel(value="TUrbanComponents对象")
public class TUrbancomponents implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "部件类型")
    private String componentType;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "下级部门")
    private String lowerDepartment;

    @ApiModelProperty(value = "部件状态")
    private String departmentStatu;

    @ApiModelProperty(value = "所属辖区")
    private String jurisdiction;

    @ApiModelProperty(value = "坐标位置")
    //@TableField(updateStrategy = FieldStrategy.IGNORED)
    private String position;

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

    @ApiModelProperty(value = "关联图片库")
    @TableField(exist = false)
    private TComponentmanagement tComponentmanagement;

    @ApiModelProperty("辖区名称/网格名称")
    @TableField(exist = false)
    private String gridName;


}
