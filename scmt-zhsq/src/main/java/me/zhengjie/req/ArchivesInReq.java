package me.zhengjie.req;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 档案转入参数
 * 
 * @author zoutao
 * @version $Id: ArchivesInReq.java, v 0.1 2020年8月14日 下午9:25:00 zoutao Exp $
 */
@ApiModel(value = "ArchivesInReq对象", description = "档案转入参数")
@Data
public class ArchivesInReq implements Serializable {

    /**  */
    private static final long serialVersionUID = 6583297755231244603L;

    @NotEmpty(message = "name is empty")
    @ApiModelProperty(value = "姓名")
    private String            name;

    @NotEmpty(message = "idCardNo is empty")
    @ApiModelProperty(value = "身份证号码")
    private String            idCardNo;

    @NotNull(message = "gender is empty")
    @ApiModelProperty(value = "性别 1、男 2、女")
    private Integer           gender;

    @NotEmpty(message = "phoneNumber is empty")
    @ApiModelProperty(value = "电话号码")
    private String            phoneNumber;

    @NotEmpty(message = "nation is empty")
    @ApiModelProperty(value = "名族")
    private String            nation;

    @NotEmpty(message = "nativePlace is empty")
    @ApiModelProperty(value = "籍贯")
    private String            nativePlace;

    @NotNull(message = "educationId is empty")
    @ApiModelProperty(value = "文化程度（：1、小学、2、初中、3、高中、4、中专、5、大专、6、本科、7、硕士、8、博士）")
    private Integer           educationId;

    //    @ApiModelProperty(value = "文化程度")
    //    private String            educationName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "birthday is empty")
    @ApiModelProperty(value = "生日")
    private Date              birthday;

    @NotEmpty(message = "unitAddress is empty")
    @ApiModelProperty(value = "单位地址")
    private String            unitAddress;

    @NotEmpty(message = "residentialAddress is empty")
    @ApiModelProperty(value = "居住地址")
    private String            residentialAddress;

    @NotEmpty(message = "organizationInfo is empty")
    @ApiModelProperty(value = "原组织信息")
    private String            organizationInfo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "organizationInfo is empty")
    @ApiModelProperty(value = "入党时间")
    private Date              partyDate;

    @NotNull(message = "partyCategoryId is empty")
    @ApiModelProperty(value = "党员类型（1:支部委员，2：支部党员，3:入党积极分子,4:新转入党员,5:退出党员,6退党党员,7:迁出党员,8:死亡党员）")
    private Integer           partyCategoryId;

    @ApiModelProperty(value = "头像")
    private String            headSculpture;

}
