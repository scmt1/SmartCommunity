package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.io.Serializable;
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
 * @author dengjie
 * @since 2020-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BasicSocialOrganization对象")
public class BasicSocialOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "社会组织类别")
    private String type;

    @ApiModelProperty(value = "社会组织名称")
    private String name;

    @ApiModelProperty(value = "所需房屋")
    private String address;

    @ApiModelProperty(value = "房屋Id")
    private String addressId;

    @ApiModelProperty(value = "法人")
    private String legalPerson;

    @ApiModelProperty(value = "法人联系方式")
    private String legalPhone;

    @ApiModelProperty(value = "法人身份证号")
    private String legalCard;

    @ApiModelProperty(value = "所属社区id")
    private String communityId;

    @ApiModelProperty(value = "所属社区名称")
    private String communityName;

    @ApiModelProperty(value = "所属街道id")
    private String streetId;

    @ApiModelProperty(value = "所属街道名称")
    private String streetName;

    @ApiModelProperty(value = "所属网格")
    private String gridsId;

    @ApiModelProperty(value = "网格长")
    private String gridPersonId;

    @ApiModelProperty(value = "网格长姓名")
    private String gridPersonName;

    @ApiModelProperty(value = "统一社会信用代码")
    private String orgCode;

    @ApiModelProperty(value = "负责人姓名")
    private String principalName;

    @ApiModelProperty(value = "负责人联系方式")
    private String principalPhone;

    @ApiModelProperty(value = "治安负责人")
    private String policePrincipal;

    @ApiModelProperty(value = "治安负责人联系方式")
    private String policePrincipalPhone;

    @ApiModelProperty(value = "头像")
    private String headPortrait;

    @ApiModelProperty(value = "是否删除 1是 0 否")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "地图标注")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String position;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "修改人")
    private String updateId;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "删除人")
    private String deleteId;

    @ApiModelProperty(value = "删除时间")
    private Timestamp deleteTime;

    @ApiModelProperty(value = "所属网格名")
    private String gridsName;

    @ApiModelProperty(value = "商户类别")
    private String merchantSort;

    @ApiModelProperty(value = "商户经营范围")
    private String businessScope;
    /**
     * 图片是否修改 用于修改时图片修改判断
     */
    @TableField(exist = false)
    private Boolean imageIsUpdate = false;

    @ApiModelProperty(value = "治安负责人名字")
    @TableField(exist = false)
    private String policePrincipalName;

}
