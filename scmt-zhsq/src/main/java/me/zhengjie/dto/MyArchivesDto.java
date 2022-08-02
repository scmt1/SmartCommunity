package me.zhengjie.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MyArchivesDto implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "姓名")
    private String            name;

    @ApiModelProperty(value = "头像")
    private String            headSculpture;

    @ApiModelProperty(value = "身份证号码")
    private String            idCardNo;

    @ApiModelProperty(value = "1、男 2、女")
    private Integer           gender;

    @ApiModelProperty(value = "党员类型")
    private String            partyCategoryName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "入党时间")
    private Date              partyDate;

    @ApiModelProperty(value = "党委名称")
    private String            partyCommitteeName;

    @ApiModelProperty(value = "党支部名称")
    private String            partyBranchName;

    @ApiModelProperty(value = "电话号码")
    private String            phoneNumber;

    @ApiModelProperty(value = "单位地址")
    private String            unitAddress;

    @ApiModelProperty(value = "家庭住址")
    private String            residentialAddress;

}
