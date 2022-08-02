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
 * 党费
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PartyDues对象", description = "党费")
public class PartyDues extends BaseEntity {

    @ApiModelProperty(value = "党员id")
    private Long    partyMemberId;

    @ApiModelProperty(value = "缴纳日期")
    private Date    payDate;

    @ApiModelProperty(value = "缴纳金额")
    private Integer amount;

    @ApiModelProperty(value = "季度")
    private Integer quarter;

    @ApiModelProperty(value = "备注")
    private String  remark;

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

}
