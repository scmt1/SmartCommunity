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
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PartyInOutApply对象", description = "")
public class PartyInOutApply extends BaseEntity {

    @ApiModelProperty(value = "姓名")
    private String  name;

    @ApiModelProperty(value = "身份证号码")
    private String  idCardNo;

    @ApiModelProperty(value = "性别 1、男 2、女")
    private Integer gender;

    @ApiModelProperty(value = "电话号码")
    private String  phoneNumber;

    @ApiModelProperty(value = "名族")
    private String  nation;

    @ApiModelProperty(value = "籍贯")
    private String  nativePlace;

    @ApiModelProperty(value = "头像")
    private String  headSculpture;

    @ApiModelProperty(value = "党员类型")
    private Integer partyCategoryId;

    @ApiModelProperty(value = "党员类型")
    private String  partyCategoryName;

    @ApiModelProperty(value = "文化程度（1、初中 2、高中 2、大专 3、本科 4、研究生 5、博士）")
    private Integer educationId;

    @ApiModelProperty(value = "文化程度")
    private String  educationName;

    @ApiModelProperty(value = "入党时间")
    private Date    partyDate;

    @ApiModelProperty(value = "生日")
    private Date    birthday;

    @ApiModelProperty(value = "单位地址")
    private String  unitAddress;

    @ApiModelProperty(value = "居住地址")
    private String  residentialAddress;

    @ApiModelProperty(value = "原组织信息")
    private String  organizationInfo;

    @ApiModelProperty(value = "申请类型（1转入，2转出）")
    private Integer applyType;

    @ApiModelProperty(value = "审核状态 1：转入 2：转出 3：通过 4：驳回")
    private Integer applyStatus;

    @ApiModelProperty(value = "驳回原因")
    private String  rejectReason;

}
