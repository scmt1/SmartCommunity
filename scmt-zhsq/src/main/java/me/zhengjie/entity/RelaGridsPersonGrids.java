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
import java.time.LocalDateTime;

/**
 * <p>
 * 网格-网格人员档案 中间表
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RelaGridsPersonGrids对象")
public class RelaGridsPersonGrids implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "网格Id")
    private String gridsId;

    @TableField(exist = false)
    private String gridsName;

    @ApiModelProperty(value = "网格人员id")
    private String gridsPersonId;

    @ApiModelProperty(value = "创建人ID")
    private String createId;

    @ApiModelProperty(value = "创建日期")
    private Timestamp createTime;

    @ApiModelProperty(value = "修改人Id")
    private String updateId;

    @ApiModelProperty(value = "修改日期")
    private Timestamp updateTime;




}
