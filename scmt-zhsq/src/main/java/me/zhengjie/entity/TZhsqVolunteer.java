package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Timestamp;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import me.zhengjie.util.IsNeeded;

/**
 * <p>
 * 街道志愿者档案
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TZhsqVolunteer对象")
public class TZhsqVolunteer implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @IsNeeded
    @ApiModelProperty(value = "姓名")
    private String name;

    @IsNeeded
    @ApiModelProperty(value = "性别")
    private String sex;

    @IsNeeded
    @ApiModelProperty(value = "头像")
    private String imgPath;

    @IsNeeded
    @ApiModelProperty(value = "是否为党员")
    private String isPartyMember;

    @IsNeeded
    @ApiModelProperty(value = "曾用名")
    private String nameUsedBefore;

    @IsNeeded
    @ApiModelProperty(value = "婚姻状况")
    private String maritalStatus;

    @IsNeeded
    @ApiModelProperty(value = "手机号")
    private String phone;

    @IsNeeded
    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @IsNeeded
    @ApiModelProperty(value = "民族")
    private String nation;

    @IsNeeded
    @ApiModelProperty(value = "籍贯")
    private String nativePlace;

    @ApiModelProperty(value = "所属街道id")
    private String streetId;

    @IsNeeded
    @ApiModelProperty(value = "所属街道")
    private String street;

    @ApiModelProperty(value = "所属社区id")
    private String communityId;

    @IsNeeded
    @ApiModelProperty(value = "所属社区")
    private String community;


    @ApiModelProperty(value = "所属网格")
    private String ownedGrid;

    @IsNeeded
    @ApiModelProperty(value = "网格名称")
    private String ownedGridName;

    @IsNeeded
    @ApiModelProperty(value = "学历")
    private String education;

    @IsNeeded
    @ApiModelProperty(value = "宗教信仰")
    private String religiousBelief;

    @IsNeeded
    @ApiModelProperty(value = "申请时间")
    private String applicationTime;

    @IsNeeded
    @ApiModelProperty(value = "工作地址")
    private String workAddress;

    @IsNeeded
    @ApiModelProperty(value = "家庭住址")
    private String homeAddress;

    @ApiModelProperty(value = "审核状态（0、审核中；1、已审核，2、审核不通过）")
    private String state;

    @ApiModelProperty(value = "地图标注")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String position;

    @ApiModelProperty(value = "排序")
    private Float orderNumber;

    @ApiModelProperty(value = "是否删除 1是 0 否")
    @TableLogic
    private Integer isDelete;

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

    @ApiModelProperty(value = "驳回原因")
    private String rejectionReasons;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "人员Id")
    private String personId;

    @ApiModelProperty(value = "房屋Id")
    private String houseId;

    @ApiModelProperty(value = "参加的活动")
    @TableField(exist=false)
    private List<TZhsqVolunteerActivity> tZhsqGridMembers;

    @ApiModelProperty(value = "人员基本信息")
    @TableField(exist=false)
    private BasicPerson basicPerson;

    /**
     * 图片是否修改 用于修改时图片修改判断
     */
    @TableField(exist = false)
    private Boolean imageIsUpdate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getIsPartyMember() {
        return isPartyMember;
    }

    public void setIsPartyMember(String isPartyMember) {
        this.isPartyMember = isPartyMember;
    }

    public String getNameUsedBefore() {
        return nameUsedBefore;
    }

    public void setNameUsedBefore(String nameUsedBefore) {
        this.nameUsedBefore = nameUsedBefore;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getStreet() {
        return street;
    }

    public void setHouseStreet(String street) {
        this.street = street;
    }

    public String getHouseCommunity() {
        return community;
    }

    public void setHouseCommunity(String community) {
        this.community = community;
    }

    public String getOwnedGridName() {
        return ownedGridName;
    }

    public void setOwnedGridName(String ownedGridName) {
        this.ownedGridName = ownedGridName;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getReligiousBelief() {
        return religiousBelief;
    }

    public void setReligiousBelief(String religiousBelief) {
        this.religiousBelief = religiousBelief;
    }

    public String getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(String applicationTime) {
        this.applicationTime = applicationTime;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }
}
