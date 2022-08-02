package me.zhengjie.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 风险源
 * </p>
 *
 * @author dengjie
 * @since 2020-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TRiskSources对象", description="风险源")
public class TRiskSources implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "风险类型 对应字典表主键")
    private String type;

    @ApiModelProperty(value = "风险名称")
    private String name;

    @TableLogic
    @ApiModelProperty(value = "状态 0 有效 1无效")
    private Integer isDelete;

    @ApiModelProperty(value = "风险图片")
    private String imgPath;

    @ApiModelProperty(value = "风险描述")
    private String description;

    @ApiModelProperty(value = "所处地点")
    private String address;

    @ApiModelProperty(value = "风险等级")
    private String riskLevel;

    @ApiModelProperty(value = "可能导致事故")
    private String mayCause;

    @ApiModelProperty(value = "地理坐标")
    private String coordinate;

    @ApiModelProperty(value = "备用1")
    private String bak1;

    @ApiModelProperty(value = "备用2")
    private String bak2;

    @ApiModelProperty(value = "备用3")
    private String bak3;

    @ApiModelProperty(value = "备用4")
    private String bak4;

    @ApiModelProperty(value = "备用5")
    private String bak5;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "修改人")
    private String updateId;

    @ApiModelProperty(value = "删除人")
    private String deleteId;

    @ApiModelProperty(value = "删除时间")
    private Timestamp deleteTime;

    /**
     * 图片是否修改 用于修改时图片修改判断
     */
    @TableField(exist = false)
    private Boolean imageIsUpdate;


}
