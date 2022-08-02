package me.zhengjie.entity.party;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 党群活动报名
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PartyMassessEnroll对象", description = "党群活动报名")
public class PartyMassessEnroll implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long              id;

    @ApiModelProperty(value = "党群活动id")
    private Long              partyMassesId;

    @ApiModelProperty(value = "用户id")
    private Long              userId;

    @ApiModelProperty(value = "用户姓名")
    private String            userName;

    @ApiModelProperty(value = "电话号码")
    private String            phoneNumber;

    @ApiModelProperty(value = "创建时间")
    private Date              createTime;

    @ApiModelProperty(value = "党员类型")
    private Integer           partyCategoryId;

    @ApiModelProperty(value = "党员类型")
    private String            partyCategoryName;

    @ApiModelProperty(value = "党委Id")
    private Long              partyCommitteeId;

    @ApiModelProperty(value = "党委名称")
    private String            partyCommitteeName;

    @ApiModelProperty(value = "党支部")
    private Long              partyBranchId;

    private Integer           isDeleted;

    @ApiModelProperty(value = "党支部名称")
    private String            partyBranchName;

}
