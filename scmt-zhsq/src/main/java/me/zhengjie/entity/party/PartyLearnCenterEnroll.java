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
 * 用户学习中心学习记录
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PartyLearnCenterEnroll对象", description = "用户学习中心学习记录")
public class PartyLearnCenterEnroll implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long              id;

    @ApiModelProperty(value = "学习中心id")
    private Long              learnCenterId;

    @ApiModelProperty(value = "用户id")
    private Long              userId;

    @ApiModelProperty(value = "用户名称")
    private String            userName;

    @ApiModelProperty(value = "用户联系电话")
    private String            userPhoneNumber;

    @ApiModelProperty(value = "党委id")
    private Long              userCommitteeId;

    @ApiModelProperty(value = "党委名称")
    private String            userCommitteeName;

    @ApiModelProperty(value = "修改时间")
    private Date              updateTime;

    @ApiModelProperty(value = "创建时间")
    private Date              createTime;

}
