package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.util.IsNeeded;
import org.springframework.format.annotation.DateTimeFormat;

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
@ApiModel(value="BasicPerson对象")
@NoArgsConstructor
@AllArgsConstructor
public class BasicPerson extends SearchVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "姓名")
    private String name;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "性别")
    private String sex;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "头像URL")
    private String imgPath;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "身份证号")
    private String cardId;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "民族")
    private String nation;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "政治面貌")
    private String politicalFace;

    @ApiModelProperty(value = "所属街道")
    private String streetId;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "所属街道")
    private String streetName;

    @ApiModelProperty(value = "所属社区")
    private String communityId;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "所属社区")
    private String communityName;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "所属网格")
    private String ownedGrid;

    @ApiModelProperty(value = "所属网格Id")
    private String ownedGridId;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "手机号")
    private String phone;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "特殊人群")
    private String specialPopulation;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "是否常驻网格 1是0否")
    private String residentGrid;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "兴趣爱好")
    private String hobby;

    @ApiModelProperty(value = "人群标签  1人口档案  2老人档案 3党员 4退伍军人 5残疾人")
    private String tableType;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "人群标签名")
    @TableField(exist = false)
    private String tableTypeName;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "人口类型")
    private String personType;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "居住地址")
    private String residentialAddress;

    @ApiModelProperty(value = "所属小区")
    private String ownedHousing;

    @ApiModelProperty(value = "楼栋Id")
    private String buildingArchiveId;

    @ApiModelProperty(value = "所住房屋编号")
    @TableField("owned_houseId")
    private String ownedHouseid;

    @ApiModelProperty(value = "房主姓名 选择房屋自动带过来")
    private String housingName;

    @ApiModelProperty(value = "与房主关系")
    private String relationShip;

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

    @ApiModelProperty(value = "坐标位置 [x,y]")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String position;

    @ApiModelProperty(value = "房屋id")
    @TableField(exist = false)
    private String ids;

    @ApiModelProperty(value = "房屋和人关联表id")
    @TableField(exist = false)
    private String relaId;



    @ApiModelProperty(value = "选中房屋")
    @TableField(exist = false)
    private List<BasicHousing> houseData;

    @ApiModelProperty(value = "老人与亲属关系")
    @TableField(exist = false)
    private List<RelaPersonRelatives> relativesList;

    /**
     * 图片是否修改 用于修改时图片修改判断
     */
    @TableField(exist = false)
    private Boolean imageIsUpdate;

    //临时传值 楼栋id
    @ApiModelProperty(value = "房屋和人是否关联")
    private Integer isBind;

    //临时传值 租客的租用状态
    @TableField(exist = false)
    @ApiModelProperty(value = "租客的租用状态")
    private String rentStatus;

    /**
     * 同户籍的人
     */
    @TableField(exist = false)
    private List<BasicPerson> AccBasicPerson;

    //临时传值 是否落户
    @TableField(exist = false)
    @ApiModelProperty(value = "是否落户")
    private  String isSettle;

    @IsNeeded
    @ApiModelProperty(value = "工作单位")
    private String workAddress;

    @IsNeeded
    @ApiModelProperty(value = "宗教信仰")
    private String religiousBelief;

    @IsNeeded
    @ApiModelProperty(value = "婚姻状况")
    private String maritalStatus;

    @IsNeeded
    @ApiModelProperty(value = "兵役情况")
    private String militaryService;

    @ApiModelProperty(value = "职业")
    private String occupation;

    @IsNeeded
    @ApiModelProperty(value = "是否优抚对象")
    private String isPreferentialTreatment;

    @IsNeeded
    @ApiModelProperty(value = "是否失孤")
    private String isLonely;

    @IsNeeded
    @ApiModelProperty(value = "是否低保")
    private String isMinimumLiving;

    @IsNeeded
    @ApiModelProperty(value = "现患大病详细")
    private String seriousIllness;

    @IsNeeded
    @ApiModelProperty(value = "是否残疾人")
    private String isDisabled;

    @IsNeeded
    @ApiModelProperty(value = "伤残类型")
    private String disabilityType;

    @IsNeeded
    @ApiModelProperty(value = "伤残等级")
    private String disabilityLevel;

    @IsNeeded
    @ApiModelProperty(value = "党员关系管理地")
    private String partyRelationshipManagemen;

    @IsNeeded
    @ApiModelProperty(value = "党员关系管理地址")
    private String partyRelationshipManagemenAddress;

    @IsNeeded
    @ApiModelProperty(value = "户籍地址")
    private String residenceAddress;

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
    @ApiModelProperty(value = "与户主关系")
    private String accRelation;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "户主身份证号")
    private String accCard;

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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPoliticalFace() {
        return politicalFace;
    }

    public void setPoliticalFace(String politicalFace) {
        this.politicalFace = politicalFace;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getOwnedGrid() {
        return ownedGrid;
    }

    public void setOwnedGrid(String ownedGrid) {
        this.ownedGrid = ownedGrid;
    }

    public String getOwnedGridId() {
        return ownedGridId;
    }

    public void setOwnedGridId(String ownedGridId) {
        this.ownedGridId = ownedGridId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialPopulation() {
        return specialPopulation;
    }

    public void setSpecialPopulation(String specialPopulation) {
        this.specialPopulation = specialPopulation;
    }

    public String getResidentGrid() {
        return residentGrid;
    }

    public void setResidentGrid(String residentGrid) {
        this.residentGrid = residentGrid;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getTableTypeName() {
        return tableTypeName;
    }

    public void setTableTypeName(String tableTypeName) {
        this.tableTypeName = tableTypeName;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getOwnedHousing() {
        return ownedHousing;
    }

    public void setOwnedHousing(String ownedHousing) {
        this.ownedHousing = ownedHousing;
    }

    public String getBuildingArchiveId() {
        return buildingArchiveId;
    }

    public void setBuildingArchiveId(String buildingArchiveId) {
        this.buildingArchiveId = buildingArchiveId;
    }

    public String getOwnedHouseid() {
        return ownedHouseid;
    }

    public void setOwnedHouseid(String ownedHouseid) {
        this.ownedHouseid = ownedHouseid;
    }

    public String getHousingName() {
        return housingName;
    }

    public void setHousingName(String housingName) {
        this.housingName = housingName;
    }

    public String getRelationShip() {
        return relationShip;
    }

    public void setRelationShip(String relationShip) {
        this.relationShip = relationShip;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getReligiousBelief() {
        return religiousBelief;
    }

    public void setReligiousBelief(String religiousBelief) {
        this.religiousBelief = religiousBelief;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMilitaryService() {
        return militaryService;
    }

    public void setMilitaryService(String militaryService) {
        this.militaryService = militaryService;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getIsPreferentialTreatment() {
        return isPreferentialTreatment;
    }

    public void setIsPreferentialTreatment(String isPreferentialTreatment) {
        this.isPreferentialTreatment = isPreferentialTreatment;
    }

    public String getIsLonely() {
        return isLonely;
    }

    public void setIsLonely(String isLonely) {
        this.isLonely = isLonely;
    }

    public String getIsMinimumLiving() {
        return isMinimumLiving;
    }

    public void setIsMinimumLiving(String isMinimumLiving) {
        this.isMinimumLiving = isMinimumLiving;
    }

    public String getSeriousIllness() {
        return seriousIllness;
    }

    public void setSeriousIllness(String seriousIllness) {
        this.seriousIllness = seriousIllness;
    }

    public String getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(String isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getDisabilityType() {
        return disabilityType;
    }

    public void setDisabilityType(String disabilityType) {
        this.disabilityType = disabilityType;
    }

    public String getDisabilityLevel() {
        return disabilityLevel;
    }

    public void setDisabilityLevel(String disabilityLevel) {
        this.disabilityLevel = disabilityLevel;
    }

    public String getPartyRelationshipManagemen() {
        return partyRelationshipManagemen;
    }

    public void setPartyRelationshipManagemen(String partyRelationshipManagemen) {
        this.partyRelationshipManagemen = partyRelationshipManagemen;
    }

    public String getPartyRelationshipManagemenAddress() {
        return partyRelationshipManagemenAddress;
    }

    public void setPartyRelationshipManagemenAddress(String partyRelationshipManagemenAddress) {
        this.partyRelationshipManagemenAddress = partyRelationshipManagemenAddress;
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(String residenceAddress) {
        this.residenceAddress = residenceAddress;
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

    public String getAccRelation() {
        return accRelation;
    }

    public void setAccRelation(String accRelation) {
        this.accRelation = accRelation;
    }

    public String getAccCard() {
        return accCard;
    }

    public void setAccCard(String accCard) {
        this.accCard = accCard;
    }
}
