/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.req;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: PartyMemberController.java, v 1.0 2020年8月13日 上午10:24:57 yongyan.pu Exp $
 */

@Data
public class PartyMemberReq implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long              id;

    @NotEmpty(message = "headSculpture is empty")
    @ApiModelProperty(value = "头像")
    private String            headSculpture;

    @NotEmpty(message = "name is empty")
    @ApiModelProperty(value = "姓名")
    private String            name;

    @NotEmpty(message = "idCardNo is empty")
    @ApiModelProperty(value = "身份证号码")
    private String            idCardNo;

    @NotNull(message = "gender is empty")
    @ApiModelProperty(value = "1、男 2、女")
    private Integer           gender;

    @NotEmpty(message = "nation is empty")
    @ApiModelProperty(value = "民族")
    private String            nation;

    @NotEmpty(message = "nativePlace is empty")
    @ApiModelProperty(value = "籍贯")
    private String            nativePlace;

    @NotNull(message = "partyCategoryId is empty")
    @ApiModelProperty(value = "党员类型")
    private Integer           partyCategoryId;

    //    @NotEmpty(message = "partyCategoryName is empty")
    //    @ApiModelProperty(value = "党员类型")
    //    private String            partyCategoryName;

    @NotNull(message = "partyDate is empty")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "入党时间")
    private Date              partyDate;

    @NotNull(message = "birthday is empty")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "生日")
    private Date              birthday;

    @NotNull(message = "educationId is empty")
    @ApiModelProperty(value = "学历 1、小学、2、初中、3、高中、4、中专、5、大专、6、本科、7、硕士、8、博士")
    private Integer           educationId;

    //    @NotEmpty(message = "educationName is empty")
    //    @ApiModelProperty(value = "学历")
    //    private String            educationName;

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

    @NotEmpty(message = "phoneNumber is empty")
    @ApiModelProperty(value = "电话号码")
    private String            phoneNumber;

    @NotEmpty(message = "unitAddress is empty")
    @ApiModelProperty(value = "单位地址")
    private String            unitAddress;

    @NotEmpty(message = "residentialAddress is empty")
    @ApiModelProperty(value = "家庭住址")
    private String            residentialAddress;
}
