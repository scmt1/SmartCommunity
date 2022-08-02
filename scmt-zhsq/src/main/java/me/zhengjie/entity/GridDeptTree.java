package me.zhengjie.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户数据权限树
 */

@Data
@ApiModel(value="用户数据权限树对象")
public class GridDeptTree {

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("名称")
    private String label;

    @ApiModelProperty(value = "子节点")
    private List<GridDeptTree> children;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "属性 1街道 2社区 3网格")
    private Integer attribute;
}
