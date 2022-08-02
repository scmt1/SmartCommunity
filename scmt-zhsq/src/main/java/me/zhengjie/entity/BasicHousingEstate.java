package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Timestamp;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import me.zhengjie.util.IsNeeded;


/**
 * <p>
 * 小区管理信息
 * </p>
 *
 * @author dengjie
 * @since 2020-07-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BasicHousingEstate对象")
public class BasicHousingEstate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "小区名称")
    private String name;

    @ApiModelProperty(value = "所属街道id")
    private String streetId;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "所属街道")
    private String street;

    @ApiModelProperty(value = "街道号")
    private Integer streetNumber;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "所属社区")
    private String community;

    @ApiModelProperty(value = "所属社区")
    private String communityId;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "所属网格")
    private String grid;

    @ApiModelProperty(value = "所属网格id")
    private String gridId;

    @ApiModelProperty(value = "物业名称Id")
    private String propertyNameId;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "物业名称")
    private String propertyName;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "物业类型")
    private String propertyType;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "物业负责人")
    private String propertyPerson;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "物业电话")
    private String propertyPhone;


    @ApiModelProperty(value = "坐标信息")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String location;

    @ApiModelProperty(value = "是否删除（0 无效 1有效）")
    @TableLogic
    private Integer isDelete;


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

    @ApiModelProperty(value = "删除时间")
    private Timestamp deleteTime;

    @ApiModelProperty(value = "账号")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "人脸识别库的id")
    private String recognitionFaceId;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "省")
    private String province;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "市")
    private String city;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "县")
    private String county;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "小区地址")
    private String address;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
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

    public String getGrid() {
        return grid;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getPropertyNameId() {
        return propertyNameId;
    }

    public void setPropertyNameId(String propertyNameId) {
        this.propertyNameId = propertyNameId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyPerson() {
        return propertyPerson;
    }

    public void setPropertyPerson(String propertyPerson) {
        this.propertyPerson = propertyPerson;
    }

    public String getPropertyPhone() {
        return propertyPhone;
    }

    public void setPropertyPhone(String propertyPhone) {
        this.propertyPhone = propertyPhone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getBak3() {
        return bak3;
    }

    public void setBak3(String bak3) {
        this.bak3 = bak3;
    }

    public String getBak4() {
        return bak4;
    }

    public void setBak4(String bak4) {
        this.bak4 = bak4;
    }

    public String getBak5() {
        return bak5;
    }

    public void setBak5(String bak5) {
        this.bak5 = bak5;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    public Timestamp getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Timestamp deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
