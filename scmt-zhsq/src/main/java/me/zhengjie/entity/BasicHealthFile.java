package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author dengjie
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BasicHealthFile对象")
public class BasicHealthFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "老人主键")
    private String personId;

    @TableField(exist = false)
    @ApiModelProperty(value = "老人姓名")
    private String personName;

    @TableField(exist = false)
    @ApiModelProperty(value = "老人性别")
    private String sex;

    @TableField(exist = false)
    @ApiModelProperty(value = "老人生日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Timestamp birthDate;

    @ApiModelProperty(value = "身高CM")
    private Integer height;

    @ApiModelProperty(value = "体重")
    private Float bodyWeight;

    @ApiModelProperty(value = "血型")
    private String bloodType;

    @ApiModelProperty(value = "腰围CM")
    private Integer waistline;

    @ApiModelProperty(value = "视力")
    private String vision;

    @ApiModelProperty(value = "血压")
    private String bloodPressure;

    @ApiModelProperty(value = "过敏反应")
    private String allergies;

    @ApiModelProperty(value = "家族病史")
    private String familyHistory;

    @ApiModelProperty(value = "既往病史")
    private String pastMedicalHistory;

    @ApiModelProperty(value = "健康状态")
    private String healthStatus;

    @ApiModelProperty(value = "当前用药")
    private String currentMedication;

    @ApiModelProperty(value = "体检报告")
    private String medicalReport;

    @ApiModelProperty(value = "抽烟情况")
    private String smokingStatus;

    @ApiModelProperty(value = "饮酒情况")
    private String drinkingStatus;

    @ApiModelProperty(value = "心理情况")
    private String psychologicalStatus;

    @ApiModelProperty(value = "运动情况")
    private String movementStatus;

    @ApiModelProperty(value = "饮食情况")
    private String dietStatus;

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

    @ApiModelProperty(value = "是否更新了体检报告 1是 0否")
    @TableField(exist = false)
    private Integer isUpdateMedicalReport;

}
