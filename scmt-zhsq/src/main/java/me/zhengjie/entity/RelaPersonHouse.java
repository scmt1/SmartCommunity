package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2020-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RelaPersonHouse对象")
public class RelaPersonHouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "人员Id")
    @TableField("person_id")
    private String personId;

    @ApiModelProperty(value = "房屋id")
    @TableField("house_id")
    private String houseId;

    @ApiModelProperty(value = "创建人ID")
    private String createId;

    @ApiModelProperty(value = "创建日期")
    private Timestamp createTime;

    @ApiModelProperty(value = "修改人Id")
    private String updateId;

    @ApiModelProperty(value = "修改日期")
    private Timestamp updateTime;

    @ApiModelProperty(value = "与户主关系")
    private String relationShip;

    @ApiModelProperty(value = "是否落户")
    private String isSettle;
}
