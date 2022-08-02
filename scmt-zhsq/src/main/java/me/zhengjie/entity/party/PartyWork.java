package me.zhengjie.entity.party;

import me.zhengjie.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 党务公开
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PartyWork对象", description = "党务公开")
public class PartyWork extends BaseEntity {

    @ApiModelProperty(value = "标题")
    private String  title;

    @ApiModelProperty(value = "党委Id")
    private Long    partyCommitteeId;

    @ApiModelProperty(value = "党委")
    private String  partyCommitteeName;

    @ApiModelProperty(value = "党委支部Id")
    private Long    partBranchId;
    @ApiModelProperty(value = "党委支部")
    private String  partBranchName;

    @ApiModelProperty(value = "党员分类Id")
    private Integer partyCategoryId;

    @ApiModelProperty(value = "党员分类")
    private String  partyCategoryName;

    @ApiModelProperty(value = "内容")
    private String  content;

}
