package me.zhengjie.entity.party;

import me.zhengjie.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 党支部
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PartyBranch对象", description = "党支部")
public class PartyBranch extends BaseEntity {

    @ApiModelProperty(value = "所属党委")
    private Long    partyCommitteeId;

    @ApiModelProperty(value = "党委名称")
    private String  partyCommitteeName;

    @ApiModelProperty(value = "组织名称")
    private String  name;

    @ApiModelProperty(value = "地址")
    private String  address;

    @ApiModelProperty(value = "手机号码")
    private String  phoneNumber;

    @ApiModelProperty(value = "书记")
    private String  secretary;

    @ApiModelProperty(value = "性别 1、男 2、女")
    private Integer gender;

}
