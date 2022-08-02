package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description:    网格辖区概况 统计
 * @Author:         ly
 * @CreateDate:     2019/5/6 13:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GridStatistics implements Serializable {
    @ApiModelProperty("统计 组织")
    @TableField(exist = false)
    private Integer socialOrganization;

    @ApiModelProperty("统计 人口")
    @TableField(exist = false)
    private Integer person;

    @ApiModelProperty("统计 房屋")
    @TableField(exist = false)
    private Integer houses;

    @ApiModelProperty("统计 建筑")
    @TableField(exist = false)
    private Integer buildingFacilities;

    @ApiModelProperty("统计 住宅")
    @TableField(exist = false)
    private Integer buildingArchives;

    @ApiModelProperty("统计 部件")
    @TableField(exist = false)
    private Integer urbancomponents;

    @ApiModelProperty("统计 物业")
    @TableField(exist = false)
    private Integer propertyArchives;
}
