package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Timestamp;
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
 * @since 2020-07-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BasicHousingManage对象")
public class BasicHousingManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "所属街道")
    private String street;

    @ApiModelProperty(value = "所属社区")
    private String community;

    @ApiModelProperty(value = "所属网格")
    private String ownedGrid;

    @ApiModelProperty(value = "小区名称")
    private String houseName;

    @ApiModelProperty(value = "房屋详址")
    private String houseAddress;

    @ApiModelProperty(value = "坐标")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String location;

    @ApiModelProperty(value = "房主姓名")
    private String houseHost;

    @ApiModelProperty(value = "身份证号")
    private String IdCard;

    @ApiModelProperty(value = "租客姓名")
    private String customerName;

    @ApiModelProperty(value = "租客身份证号")
    @TableField("customer_IdCard")
    private String customerIdcard;

    @ApiModelProperty(value = "租客电话")
    private String customerMobile;

    @ApiModelProperty(value = "房屋类型")
    private String houseType;

    @ApiModelProperty(value = "租用状态")
    private String rentStatus;

    @ApiModelProperty(value = "住户数量")
    private Integer houseNumber;

    @ApiModelProperty(value = "是否绑定地图")
    @TableField("is_bindMap")
    private Integer isBindmap;

    @ApiModelProperty(value = "删除标识")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "创建人ID")
    private String createId;

    @ApiModelProperty(value = "创建日期")
    private Timestamp createTime;

    @ApiModelProperty(value = "修改人Id")
    private String updateId;

    @ApiModelProperty(value = "修改日期")
    private Timestamp updateTime;

    @ApiModelProperty(value = "备用1")
    private String bak1;

    @ApiModelProperty(value = "备用2")
    private String bak2;

    @ApiModelProperty(value = "备用3")
    private String bak3;

    @ApiModelProperty(value = "楼栋id")
    private String buildArchivesId;

    @ApiModelProperty(value = "楼栋名称")
    private String buildArchivesName;

    @ApiModelProperty(value = "门牌号")
    private String doorNumber;

    @ApiModelProperty(value = "户主关系")
    @TableField(exist = false)
    private String relationShip;

}
