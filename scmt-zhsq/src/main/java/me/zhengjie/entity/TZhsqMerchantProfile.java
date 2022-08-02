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
 * @since 2020-07-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TZhsqMerchantProfile对象")
public class TZhsqMerchantProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "商户类别")
    private String merchantSort;

    @ApiModelProperty(value = "商户名称")
    private String merchantName;

    @ApiModelProperty(value = "商户地址")
    private String merchantAddress;

    @ApiModelProperty(value = "经营范围")
    private String businessScope;

    @ApiModelProperty(value = "法人姓名")
    private String legalEntity;

    @ApiModelProperty(value = "法人联系电话")
    private String legalPhone;

    @ApiModelProperty(value = "法人身份证号")
    private String legalId;

    @ApiModelProperty(value = "经纬度")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String location;

    @ApiModelProperty(value = "所属街道")
    private String street;

    @ApiModelProperty(value = "所属街道id")
    private String streetId;

    @ApiModelProperty(value = "所属社区id")
    private String legalCommunityId;

    @ApiModelProperty(value = "所属社区")
    private String legalCommunity;

    @ApiModelProperty(value = "所属网格")
    private String legalGrid;

    @ApiModelProperty(value = "所属网格id")
    private String gridId;

    @ApiModelProperty(value = "头像")
    private String headPortrait;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    @ApiModelProperty(value = "房屋Id")
    private String houseId;

    @ApiModelProperty(value = "房屋名称")
    private String houseName;

    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "是否被删除   0没有删除 1删除")
    @TableLogic
    private Integer isDelete;

    /**
     * 图片是否修改 用于修改时图片修改判断
     */
    @TableField(exist = false)
    private Boolean imageIsUpdate = false;


}
