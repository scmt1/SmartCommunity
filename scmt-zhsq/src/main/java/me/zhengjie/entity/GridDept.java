package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.entity.BaseEntity;
import me.zhengjie.system.domain.Dept;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("部门")
@TableName("grid_dept")
public class GridDept extends BaseEntity {

    @ApiModelProperty("名称")
    private String name;

    /**
     * 顶级为0
     */
    @ApiModelProperty("父类id")
    private String parentId;

    @ApiModelProperty("主管")
    private Long supervisor;

    @ApiModelProperty("类别")
    private Integer type;

    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("地址")
    private String address;

    /**
     * 1：街道
     * 2：社区
     * 3：普通
     */
    @ApiModelProperty("属性")
     private Integer attribute;

    @ApiModelProperty("简介")
    private String introduction;

    @TableField(exist = false)
    private List<Dept> childList = new ArrayList<>();

    @TableField(exist = false)
    private String parentName;

    @TableField(exist = false)
    private String supervisorName;

    @ApiModelProperty("名称")
    @TableField(exist = false)
    private String label;

    @ApiModelProperty(value = "子节点")
    @TableField(exist = false)
    private List<GridDept> children;

}
