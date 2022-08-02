package me.zhengjie.entity.party;

import java.io.Serializable;
import java.util.Date;

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
 * @since 2020-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PartyMessage对象", description = "")
public class PartyMessage implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    private Long              id;

    @ApiModelProperty(value = "1、党务公开 2、党群活动")
    private Integer           msgType;

    @ApiModelProperty(value = "党务id")
    private Long              partyCommitteeId;

    @ApiModelProperty(value = "支部id")
    private Long              partyBranchId;

    @ApiModelProperty(value = "内容")
    private String            content;

    @ApiModelProperty(value = "创建时间")
    private Date              createTime;

}
