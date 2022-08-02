package me.zhengjie.entity.party;

import java.util.Date;

import me.zhengjie.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 党群 
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PartyMasses对象", description = "党群 ")
public class PartyMasses extends BaseEntity {

    @ApiModelProperty(value = "标题")
    private String  title;

    @ApiModelProperty(value = "党员类型")
    private Integer partyCategoryId;

    @ApiModelProperty(value = "党员类型")
    private String  partyCategoryName;

    @ApiModelProperty(value = "党委Id")
    private Long    partyCommitteeId;

    @ApiModelProperty(value = "党委名称")
    private String  partyCommitteeName;

    @ApiModelProperty(value = "党支部")
    private Long    partyBranchId;

    @ApiModelProperty(value = "党支部名称")
    private String  partyBranchName;

    @ApiModelProperty(value = "活动时间")
    private Date    activityDate;

    @ApiModelProperty(value = "活动状态 1、进行中 2、结束 3、取消")
    private Integer massesStatus;

    @ApiModelProperty(value = "内容")
    private String  content;

}
