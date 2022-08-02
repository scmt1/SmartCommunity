package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import me.zhengjie.util.IsNeeded;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author dengjie
 * @since 2020-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BasicHousing对象")
public class BasicHousing implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @IsNeeded
    @ApiModelProperty(value = "房屋编码")
    private String houseCode;

    @ApiModelProperty(value = "楼栋id")
    private String buildArchiveId;

    @IsNeeded
    @ApiModelProperty(value = "楼栋名称")
    private String buildArchiveName;

    @IsNeeded
    @ApiModelProperty(value = "单元")
    private String unit;

    @IsNeeded
    @ApiModelProperty(value = "楼层")
    private String floor;

    @IsNeeded
    @ApiModelProperty(value = "门牌号")
    private String doorNumber;

    @IsNeeded
    @ApiModelProperty(value = "房屋面积")
    private String area;

    @IsNeeded
    @ApiModelProperty(value = "房屋类型")
    private String houseType;

    @IsNeeded
    @ApiModelProperty(value = "房型")
    private String houseForm;

    @IsNeeded
    @ApiModelProperty(value = "房屋产权")
    private String houseProperty;

    @IsNeeded
    @ApiModelProperty(value = "房屋分类")
    private String houseClassification;

    @IsNeeded
    @ApiModelProperty(value = "房屋性质")
    private String houseNature;

    @IsNeeded
    @ApiModelProperty(value = "所属街道")
    private String street;

    @ApiModelProperty(value = "所属街道Id")
    private String streetId;

    @IsNeeded
    @ApiModelProperty(value = "所属社区")
    private String community;

    @ApiModelProperty(value = "所属社区Id")
    private String communityId;

    @ApiModelProperty(value = "所属网格Id")
    private String ownedGridId;

    @IsNeeded
    @ApiModelProperty(value = "所属网格")
    private String ownedGrid;

    @ApiModelProperty(value = "小区Id")
    private String houseId;

    @IsNeeded
    @ApiModelProperty(value = "小区名称")
    private String houseName;

    @IsNeeded
    @ApiModelProperty(value = "房主姓名")
    private String hostName;

    @IsNeeded
    @ApiModelProperty(value = "身份证号")
    private String hostCard;

    @IsNeeded
    @ApiModelProperty(value = "房屋详址")
    private String houseAddress;

    @IsNeeded
    @ApiModelProperty(value = "户号")
    private String accNumber;

    @IsNeeded
    @ApiModelProperty(value = "户别")
    private String accType;

    @IsNeeded
    @ApiModelProperty(value = "户主姓名")
    private String accName;

    @IsNeeded
    @ApiModelProperty(value = "户主身份证号")
    private String accCard;

    @ApiModelProperty(value = "街道名称")
    private String streetName;

    @ApiModelProperty(value = "街道号")
    private String streetNumber;

    @ApiModelProperty(value = "租客姓名")
    private String customerName;

    @ApiModelProperty(value = "租客身份证号")
    private String customerCard;

    @ApiModelProperty(value = "租客电话")
    private String customerMobile;


    @ApiModelProperty(value = "是否绑定地图")
    private Integer isMap;

    @ApiModelProperty(value = "租用状态")
    private String rentStatus;

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

    @TableField(exist = false)
    @ApiModelProperty(value = "租客")
    private List<BasicPerson> personData;

    @ApiModelProperty(value = "与户主关系")
    @TableField(exist = false)
    private String relationship;


    @ApiModelProperty(value = "用户id")
    @TableField(exist = false)
    private String personId;

    @ApiModelProperty(value = "房屋楼栋中心坐标")
    @TableField(exist = false)
    private String position;

    @ApiModelProperty(value = "房屋编码")
    private String accRelation;

    @ApiModelProperty(value = "房屋是否落户")
//    @TableField(exist = false)
    private String isSettle;

    @ApiModelProperty(value = "楼栋详细信息")
    @TableField(exist = false)
    private TBuildingArchives buildingArchives;

    @ApiModelProperty(value = "单元Id")
    private String unitId;

    @ApiModelProperty(value = "房主性别")
    private String hostGender;

    @ApiModelProperty(value = "房主户籍地址")
    private String hostPermanentAddress;

    @ApiModelProperty(value = "房屋性质描述")
    private String houseNatureDescribe;

    @ApiModelProperty(value = "房主现居地址")
    private String hostCurrentAddress;

    @ApiModelProperty(value = "有无物业管理")
    private String haveProperty;

    @ApiModelProperty(value = "房主联系方式")
    private String hostContact;

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getBuildArchiveId() {
        return buildArchiveId;
    }

    public void setBuildArchiveId(String buildArchiveId) {
        this.buildArchiveId = buildArchiveId;
    }

    public String getBuildArchiveName() {
        return buildArchiveName;
    }

    public void setBuildArchiveName(String buildArchiveName) {
        this.buildArchiveName = buildArchiveName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getHouseForm() {
        return houseForm;
    }

    public void setHouseForm(String houseForm) {
        this.houseForm = houseForm;
    }

    public String getHouseProperty() {
        return houseProperty;
    }

    public void setHouseProperty(String houseProperty) {
        this.houseProperty = houseProperty;
    }

    public String getHouseClassification() {
        return houseClassification;
    }

    public void setHouseClassification(String houseClassification) {
        this.houseClassification = houseClassification;
    }

    public String getHouseNature() {
        return houseNature;
    }

    public void setHouseNature(String houseNature) {
        this.houseNature = houseNature;
    }

    public String getStreet() {
        return street;
    }

    public void setHouseStreet(String street) {
        this.street = street;
    }

    public String getSteetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getHouseCommunity() {
        return community;
    }

    public void setHouseCommunity(String community) {
        this.community = community;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getOwnedGridId() {
        return ownedGridId;
    }

    public void setOwnedGridId(String ownedGridId) {
        this.ownedGridId = ownedGridId;
    }

    public String getOwnedGrid() {
        return ownedGrid;
    }

    public void setOwnedGrid(String ownedGrid) {
        this.ownedGrid = ownedGrid;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostCard() {
        return hostCard;
    }

    public void setHostCard(String hostCard) {
        this.hostCard = hostCard;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAccCard() {
        return accCard;
    }

    public void setAccCard(String accCard) {
        this.accCard = accCard;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCard() {
        return customerCard;
    }

    public void setCustomerCard(String customerCard) {
        this.customerCard = customerCard;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }
}
