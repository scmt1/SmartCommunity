package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * @since 2020-07-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TBuildingArchives对象")
public class TBuildingArchives implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @IsNeeded
    @ApiModelProperty(value = "建筑名称")
    private String buildingName;

    @ApiModelProperty(value = "所属小区")
    private String housingEstate;

    @IsNeeded
    @ApiModelProperty(value = "小区名")
    @TableField(exist = false)
    private String housingEstateName;

    @IsNeeded
    @ApiModelProperty(value = "网格名")
    @TableField(exist = false)
    private String gridName;

    @ApiModelProperty(value = "所属网格")
    private String grid;

    @IsNeeded
    @ApiModelProperty(value = "所属社区")
    private String community;

    @ApiModelProperty(value = "所属社区Id")
    private String communityId;

    @IsNeeded
    @ApiModelProperty(value = "所属街道")
    private String street;

    @ApiModelProperty(value = "所属街道Id")
    private String streetId;


    @ApiModelProperty(value = "建筑类型")
    private String buildingType;

    @IsNeeded
    @ApiModelProperty(value = "建筑类型名")
    @TableField(exist = false)
    private String buildingTypeName;


    @ApiModelProperty(value = "建筑年代")
    private Timestamp buildingYear;

    @IsNeeded
    @TableField(exist = false)
    @ApiModelProperty(value = "建筑年代")
    private String buildingYearString;

    @IsNeeded
    @ApiModelProperty(value = "建筑面积")
    private String area;

    @IsNeeded
    @ApiModelProperty(value = "建筑结构")
    private String buildingStructure;

    @IsNeeded
    @ApiModelProperty(value = "建筑性质")
    private String buildingNature;

    @IsNeeded
    @ApiModelProperty(value = "建筑地址")
    private String buildingAddress;

    @IsNeeded
    @ApiModelProperty(value = "楼栋长姓名")
    private String managerName;

    @IsNeeded
    @ApiModelProperty(value = "楼栋长电话")
    private String managerPhone;

    @IsNeeded
    @ApiModelProperty(value = "简介")
    private String profile;

    @IsNeeded
    @ApiModelProperty(value = "单元")
    @TableField(exist = false)
    private String unit;

    @IsNeeded
    @ApiModelProperty(value = "楼层")
    @TableField(exist = false)
    private String floor;

    @IsNeeded
    @ApiModelProperty(value = "每层的户数")
    @TableField(exist = false)
    private String doorNumber;

    @ApiModelProperty(value = "地图标注（地理坐标）")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String location;

    @ApiModelProperty(value = "街道号")
    private Integer streetNumber;
    //position
    @ApiModelProperty(value = "中心点")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String position;

    @ApiModelProperty(value = "是否删除（0 无效 1有效）")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "备用2")
    private String bak2;

    @ApiModelProperty(value = "备用3")
    private String bak3;

    @ApiModelProperty(value = "备用4")
    private String bak4;

    @ApiModelProperty(value = "备用5")
    private String bak5;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "修改人")
    private String updateId;

    @ApiModelProperty(value = "删除人")
    private String deleteId;

    @ApiModelProperty(value = "楼栋图片")
    private String imgPath;

    @TableField(exist = false)
    private Boolean imageIsUpdate;

    @ApiModelProperty(value = "车位数")
    private String parkPlaceNumber;

    @ApiModelProperty(value = "电梯数")
    private String elevatorNumber;


    @ApiModelProperty(value = "单元")
    @TableField(exist = false)
    private List<BasicUnit> unitList;

    @ApiModelProperty(value = "小区")
    @TableField(exist = false)
    private Object BasicHousingEstate;

    @ApiModelProperty(value = "是否是列表上修改坐标")
    @TableField(exist = false)
    private int isUpdateLocation;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getHousingEstate() {
        return housingEstate;
    }

    public void setHousingEstate(String housingEstate) {
        this.housingEstate = housingEstate;
    }

    public String getHousingEstateName() {
        return housingEstateName;
    }

    public void setHousingEstateName(String housingEstateName) {
        this.housingEstateName = housingEstateName;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public String getGrid() {
        return grid;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public String getBuildingTypeName() {
        return buildingTypeName;
    }

    public void setBuildingTypeName(String buildingTypeName) {
        this.buildingTypeName = buildingTypeName;
    }

    public Timestamp getBuildingYear() {
        return buildingYear;
    }

    public void setBuildingYear(Timestamp buildingYear) {
        this.buildingYear = buildingYear;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBuildingStructure() {
        return buildingStructure;
    }

    public void setBuildingStructure(String buildingStructure) {
        this.buildingStructure = buildingStructure;
    }

    public String getBuildingNature() {
        return buildingNature;
    }

    public void setBuildingNature(String buildingNature) {
        this.buildingNature = buildingNature;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
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

    public String getBuildingYearString() {
        return buildingYearString;
    }

    public void setBuildingYearString(String buildingYearString) {
        this.buildingYearString = buildingYearString;
    }

}
