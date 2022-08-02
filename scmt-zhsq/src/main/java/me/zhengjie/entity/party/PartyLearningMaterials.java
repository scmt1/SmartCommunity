package me.zhengjie.entity.party;

import me.zhengjie.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 党建学习资料（模范宣塑,两学一做)
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="PartyLearningMaterials对象", description="党建学习资料（模范宣塑,两学一做)")
public class PartyLearningMaterials extends BaseEntity {


    @ApiModelProperty(value = "网站名称")
    private String siteName;

    @ApiModelProperty(value = "网站地址")
    private String siteUrl;

    @ApiModelProperty(value = "资源来源(1:两学一做，2：模范宣塑)")
    private Integer sourceType;


}
