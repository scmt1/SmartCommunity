package me.zhengjie.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.sql.Timestamp;
/**
 * <p>
 *
 * </p>
 *
 * @author dengjie
 * @since 2020-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "TCompany对象", description = "")
public class TCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "企业名")
    private String companyName;

    @ApiModelProperty(value = "企业责任人")
    private String responsible;

    @ApiModelProperty(value = "企业xyz地理位置")
    private String positionNum;

    @ApiModelProperty(value = "地理位置")
    private String positionChar;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "是否被删除 0 没有删除 1删除")
    private Integer isDelete;

    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    @ApiModelProperty(value = "企业的性质，如国企、民企")
    private String companyNature;

    @ApiModelProperty(value = "企业联系电话")
    private String companyPhone;

    @ApiModelProperty(value = "企业邮箱")
    private String companyEmail;

    @ApiModelProperty(value = "营业执照")
    private String businessLicence;

    @ApiModelProperty(value = "组织机构代码 ")
    private String orgCode;

    @ApiModelProperty(value = "成立日期")
    private Timestamp foundDate;

    @ApiModelProperty(value = "所在地址")
    private String registryAddress;

    @ApiModelProperty(value = "行业类别")
    private String industryType;

    @ApiModelProperty(value = "企业隶属关系")
    private String enterpriseRelation;

    @ApiModelProperty(value = "注册登记类型")
    private String registryType;

    @ApiModelProperty(value = "邮政编码")
    private String postCode;

    @ApiModelProperty(value = "企业规模")
    private String enterpriseScale;

    @ApiModelProperty(value = "四上企业分类")
    private String fourEnterprise;

    @ApiModelProperty(value = "上年末从业人数")
    @TableField("people_engaged_lastYear")
    private Integer peopleEngagedLastyear;

    @ApiModelProperty(value = "上年营业收入(万元")
    @TableField("incom_lastYear")
    private String incomLastyear;

    @ApiModelProperty(value = "资产总额(万元)")
    private String totalAssets;

    @ApiModelProperty(value = "经营地址")
    private String businessAddress;

    @ApiModelProperty(value = "经营范围")
    private String businessScope;

    @ApiModelProperty(value = "重点监管级别")
    private String keyRegulatoryLevel;

    @ApiModelProperty(value = "营业状态")
    private String businessStatus;

    @ApiModelProperty(value = "上级企业名称")
    @TableField("upEnterprise_name")
    private String upenterpriseName;

    @ApiModelProperty(value = "上级企业组织机构代码")
    private String upOrgCode;

    @ApiModelProperty(value = "举报人")
    private String reporter;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "安全监管主管机构")
    private String
            safetySupervisionUthority;

    @ApiModelProperty(value = "本单位安全生产管理机构")
    private String unitSafetyManage;

    @ApiModelProperty(value = "专职安全管理人数")
    private Integer safetyPersonNum;

    @ApiModelProperty(value = "安全专业技术人数")
    private Integer safetyTecNum;

    @ApiModelProperty(value = "单位主要负责人")
    private String unitResponsePerson;

    @ApiModelProperty(value = "主要负责人联系方式")
    private String responsePersonMobile;

    @ApiModelProperty(value = "分管安全负责人")
    private String chargeSafetyPerson;

    @ApiModelProperty(value = "分管安全负责人联系方式")
    private String safetyPersonMobile;

    @ApiModelProperty(value = "安全管理机构负责人")
    private String safetyManageResponser;

    @ApiModelProperty(value = "安全管理机构负责人联系方式")
    private String manageResponserMobile;

}
