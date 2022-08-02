package me.zhengjie.dao.entity;

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
 * @since 2020-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "TShooting对象", description = "")
public class TShooting implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "隐患等级 对应字典表主键")
    private String level;

    @ApiModelProperty(value = "隐患类型 对应字典表主键")
    private String type;

    @ApiModelProperty(value = "隐患名称")
    private String name;

    @TableLogic
    @ApiModelProperty(value = "状态 0 无效 1有效")
    private Integer state;

    @ApiModelProperty(value = "隐患坐标【x,y】")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String position;

    @ApiModelProperty(value = "隐患描述")
    private String description;

    @ApiModelProperty(value = "检查地点")
    private String address;

    @ApiModelProperty(value = "违规部门")
    private String depId;

    @ApiModelProperty(value = "违规部门")
    private String depName;

    @ApiModelProperty(value = "风险等级")
    private String riskLevel;

    @ApiModelProperty(value = "可能导致事故")
    private String mayCause;

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

    @ApiModelProperty(value = "图片路径")
    @TableField("img_path")
    private String imgPath;

    @ApiModelProperty(value = "视频路径")
    @TableField("video_path")
    private String videoPath;

    /**
     * 图片是否修改 用于修改时图片修改判断
     */
    @TableField(exist = false)
    private Boolean imageIsUpdate;

    /**
     * 视频是否修改 用于修改时图片修改判断
     */
    @TableField(exist = false)
    private Boolean videoIsUpdate;


    @ApiModelProperty(value = "清单类别")
    @TableField("category")
    private String category;

    @ApiModelProperty(value = "专项分类")
    @TableField("classification")
    private String classification;

    @ApiModelProperty(value = "检查频率")
    @TableField("check_frequency")
    private String checkFrequency;

    @ApiModelProperty(value = "清单编制日期")
    @TableField("prepared_date")
    private Timestamp preparedDate;

}
