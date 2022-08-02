package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
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
@ApiModel(value="物业对象")
public class TZhsqPropertyManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "物业名称")
    private String propertyName;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "物业类型")
    private String propertyType;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "物业负责人")
    private String propertyPrincipal;

    @IsNeeded//需要excel导入的字段
    @ApiModelProperty(value = "物业负责人电话")
    private String propertyPrincipalPhone;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "修改人")
    private String updateId;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "是否被删除   0没有删除 1删除")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "修改人")
    @TableField(exist = false)
    private String grid;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPropertyPrincipal() {
        return propertyPrincipal;
    }

    public void setPropertyPrincipal(String propertyPrincipal) {
        this.propertyPrincipal = propertyPrincipal;
    }

    public String getPropertyPrincipalPhone() {
        return propertyPrincipalPhone;
    }

    public void setPropertyPrincipalPhone(String propertyPrincipalPhone) {
        this.propertyPrincipalPhone = propertyPrincipalPhone;
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

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
