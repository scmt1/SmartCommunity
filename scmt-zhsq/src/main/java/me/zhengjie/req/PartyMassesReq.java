package me.zhengjie.req;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 党群 
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Data
public class PartyMassesReq implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "title is empty")
    @ApiModelProperty(value = "标题")
    private String            title;

    @NotNull(message = "partyCategoryId is empty")
    @ApiModelProperty(value = "党员类型")
    private Integer           partyCategoryId;

    //    @ApiModelProperty(value = "党员类型")
    //    private String            partyCategoryName;

    @NotNull(message = "partyCommitteeId is empty")
    @ApiModelProperty(value = "党委Id")
    private Long              partyCommitteeId;

    @NotEmpty(message = "partyCommitteeName is empty")
    @ApiModelProperty(value = "党委名称")
    private String            partyCommitteeName;

    @NotNull(message = "partyBranchId is empty")
    @ApiModelProperty(value = "党支部")
    private Long              partyBranchId;

    @NotEmpty(message = "partyBranchName is empty")
    @ApiModelProperty(value = "党支部名称")
    private String            partyBranchName;

    @NotNull(message = "activityDate is empty")
    @ApiModelProperty(value = "活动时间")
    private Date              activityDate;

    @NotEmpty(message = "content is empty")
    private String            content;

}
