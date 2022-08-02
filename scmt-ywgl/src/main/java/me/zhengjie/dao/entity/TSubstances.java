package me.zhengjie.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @since 2020-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TSubstances对象", description="")
public class TSubstances implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "物质名称")
    private String name;

    @ApiModelProperty(value = "规格型号")
    private String specificationModel;

    @ApiModelProperty(value = "标准")
    private String standard;

    @ApiModelProperty(value = "数量")
    private Long number;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "所属单位")
    private String affiliatedUnit;

    @ApiModelProperty(value = "存放位置")
    private String parkingPosition;

    @ApiModelProperty(value = "地图位置")
    private String mapLocation;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "修改人")
    private String updateId;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "备用")
    private String bak1;

    @ApiModelProperty(value = "备用")
    private String bak2;

    @ApiModelProperty(value = "备用")
    private String bak3;

    @ApiModelProperty(value = "备用")
    private String bak4;

    @ApiModelProperty(value = "备用")
    private String bak5;

    @TableLogic
    @ApiModelProperty(value = "删除标志（0，已删除，1未删除）")
    private Integer isdelete;

}
