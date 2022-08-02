package me.zhengjie.entity.party;

import java.util.Date;

import me.zhengjie.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 党员基本信息
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PartyMember对象", description = "党员基本信息")
public class PartyMember extends BaseEntity {

    @ApiModelProperty(value = "姓名")
    private String  name;

    @ApiModelProperty(value = "头像")
    private String  headSculpture;

    @ApiModelProperty(value = "身份证号码")
    private String  idCardNo;

    @ApiModelProperty(value = "1、男 2、女")
    private Integer gender;

    @ApiModelProperty(value = "民族")
    private String  nation;

    @ApiModelProperty(value = "籍贯")
    private String  nativePlace;

    @ApiModelProperty(value = "党员类型")
    private Integer partyCategoryId;

    @ApiModelProperty(value = "党员类型")
    private String  partyCategoryName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "入党时间")
    private Date    partyDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生日")
    private Date    birthday;

    @ApiModelProperty(value = "学历")
    private Integer educationId;

    @ApiModelProperty(value = "1、初中 2、高中 2、大专 3、本科 4、研究生 5、博士")
    private String  educationName;

    @ApiModelProperty(value = "党委Id")
    private Long    partyCommitteeId;

    @ApiModelProperty(value = "党委名称")
    private String  partyCommitteeName;

    @ApiModelProperty(value = "党支部")
    private Long    partyBranchId;

    @ApiModelProperty(value = "党支部名称")
    private String  partyBranchName;

    @ApiModelProperty(value = "电话号码")
    private String  phoneNumber;

    @ApiModelProperty(value = "单位地址")
    private String  unitAddress;

    @ApiModelProperty(value = "家庭住址")
    private String  residentialAddress;

    @ApiModelProperty(value = "转入 转出 状态 0、默认  1、转入 2、转出")
    private Integer isInto;

    @ApiModelProperty(value = "转入时间")
    private Date    intoTime;

    @ApiModelProperty("业务状态")
    private Integer status;

}
