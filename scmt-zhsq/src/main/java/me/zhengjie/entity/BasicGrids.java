package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
 *
 * </p>
 *
 * @author dengjie
 * @since 2020-07-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BasicGrids对象")
public class BasicGrids implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "网格名称")
    private String name;


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

    @ApiModelProperty(value = "网格长")
    private String gridPersonId;

    @ApiModelProperty(value = "网格长姓名")
    private String gridPersonName;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "党组织")
    private String organization;

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


    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "面积")
    private String area;



    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "网格概况")
    private String information;

    @ApiModelProperty(value = "类型")
    private String type;



    @ApiModelProperty(value = "账号")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "人脸识别库的id")
    private String recognitionFaceId;


    @ApiModelProperty(value = "备注")
    private String remark;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
