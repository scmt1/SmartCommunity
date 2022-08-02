package me.zhengjie.entity.party;

import me.zhengjie.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 党委
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="PartyCommittee对象", description="党委")
public class PartyCommittee extends BaseEntity {


    @ApiModelProperty(value = "组织名称")
    private String name;

    @ApiModelProperty(value = "街道id")
    private Long streetId;

    @ApiModelProperty(value = "街道名称")
    private String streetName;

    @ApiModelProperty(value = "社区Id")
    private Long communityId;

    @ApiModelProperty(value = "社区名称")
    private String communityName;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "书记")
    private String secretary;

    @ApiModelProperty(value = "性别 1、男 2、女")
    private Integer gender;


}
