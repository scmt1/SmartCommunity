package me.zhengjie.entity.party;

import me.zhengjie.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 三会一课与参会人关系
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PartyLessonsMember对象", description = "三会一课与参会人关系")
public class PartyLessonsMember extends BaseEntity {

    @ApiModelProperty(value = "课程id")
    private Long   partyLessonsId;

    @ApiModelProperty(value = "党员id")
    private Long   partyMemberId;

    @ApiModelProperty(value = "党员名称（冗余字段）")
    private String partyMemberName;

    @ApiModelProperty(value = "党员头像（冗余字段）")
    private String memberHeadSculpture;

}
