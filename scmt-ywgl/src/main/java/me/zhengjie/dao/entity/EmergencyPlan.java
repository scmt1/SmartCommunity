package me.zhengjie.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author dengjie
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EmergencyPlan对象", description="")
public class EmergencyPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预案Id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "方案描述")
    @TableField("emerg_description")
    private String emergDescription;

    @ApiModelProperty(value = "预案名称")
    private String emergName;

    @ApiModelProperty(value = "预案制定日期")
    private Timestamp emergMakeDate;

    @ApiModelProperty(value = "预案方案坐标点")
    private String emergMap;

    @ApiModelProperty(value = "预案类型")
    private String emergType;

    @ApiModelProperty(value = "预案等级")
    private String emergLevel;

    @ApiModelProperty(value = "预案制定单位")
    private String emergMakeDept;

    @ApiModelProperty(value = "预案制定单位Id")
    @TableField("emerg_make_deptId")
    private String emergMakeDeptid;

    @ApiModelProperty(value = "预案执行部门")
    private String emergImplDept;

    @ApiModelProperty(value = "预案执行部门Id")
    @TableField("emerg_impl_deptId")
    private String emergImplDeptid;

    @ApiModelProperty(value = "预案制定人员")
    private String emergMakePerson;

    @ApiModelProperty(value = "预案制定人员Id")
    @TableField("emerg_make_personId")
    private String emergMakePersonid;

	@TableLogic
    @ApiModelProperty(value = "删除标识（0-未删除，1-删除）")
    private Integer delFlag;

    @ApiModelProperty(value = "创建人id")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "修改人")
    private String updateId;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;


}
