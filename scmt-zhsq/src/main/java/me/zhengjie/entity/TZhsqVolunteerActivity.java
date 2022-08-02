package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.sql.Timestamp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 志愿者活动
 * </p>
 *
 * @author dengjie
 * @since 2020-07-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TZhsqVolunteerActivity对象")
public class TZhsqVolunteerActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "活动地址")
    private String activitiyAddress;

    @ApiModelProperty(value = "活动开始时间")
    private Timestamp startDate1;

    @ApiModelProperty(value = "活动结束时间")
    private Timestamp endDate1;

    @ApiModelProperty(value = "组织者")
    private String organizer;

    @ApiModelProperty(value = "参与志愿者")
    private String participant;

    @TableField(exist = false)
    private String participantName;

    @ApiModelProperty(value = "活动简介")
    private String activityProfile;

    @ApiModelProperty(value = "地图标注")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String position;

    @ApiModelProperty(value = "排序")
    private Float orderNumber;

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


}
