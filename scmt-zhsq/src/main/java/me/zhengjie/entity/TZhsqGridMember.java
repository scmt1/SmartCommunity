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
 * 网格员
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TZhsqGridMember对象")
public class TZhsqGridMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @IsNeeded
    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    @IsNeeded
    private String sex;

    @ApiModelProperty(value = "头像")
    @IsNeeded
    private String headPortrait;

    @ApiModelProperty(value = "政治面貌")
    @IsNeeded
    private String type;

    @ApiModelProperty(value = "民族")
    @IsNeeded
    private String nation;

    @ApiModelProperty(value = "手机号")
    @IsNeeded
    private String phone;

    @ApiModelProperty(value = "身份证号")
    @IsNeeded
    private String idCard;

    @ApiModelProperty(value = "个人简介")
    @IsNeeded
    private String personalProfile;

    @ApiModelProperty(value = "出生日期")
    private Timestamp birthday;

    @ApiModelProperty(value = "固定电话")
    private String fixedTelephone;

    @ApiModelProperty(value = "所属街道")
    private String streetId;

    @ApiModelProperty(value = "所属街道")
    @IsNeeded
    private String streetName;

    @ApiModelProperty(value = "所属社区")
    private String communityId;

    @ApiModelProperty(value = "所属社区")
    @IsNeeded
    private String communityName;

    @ApiModelProperty(value = "所属网格")
    @IsNeeded
    @TableField(exist = false)
    private String gridName;

    @ApiModelProperty(value = "所属岗位")
    @IsNeeded
    private String post;

    @ApiModelProperty(value = "级别")
    private String postLevel;

    @ApiModelProperty(value = "职责范畴")
    @IsNeeded
    private String responsibilities;

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

    /**
     * 图片是否修改 用于修改时图片修改判断
     */
    @TableField(exist = false)
    private Boolean imageIsUpdate = false;

    /**
     * 网格id  用于保存中间表
     */
    @TableField(exist = false)
    private String[] grid;

    /**
     * 网格人员 网格中间表信息
     */
    @TableField(exist = false)
    private List<RelaGridsPersonGrids> GridsPersonGrids;

    /**
     * 所属网格  查询使用
     */
    @TableField(exist = false)
    private String ownedGrid ;

    /**
     * 所属网格Id  查询使用
     */
    @TableField(exist = false)
    private String gridId ;

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

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
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

    public String getPersonalProfile() {
        return personalProfile;
    }

    public void setPersonalProfile(String personalProfile) {
        this.personalProfile = personalProfile;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }
}
