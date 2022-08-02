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
 * 
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PartyCommend对象", description = "")
public class PartyCommend extends BaseEntity {

    @ApiModelProperty(value = "党员id")
    private Long   partyMemberId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "表彰时间")
    private Date   commendDate;

}
