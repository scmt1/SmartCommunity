package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@AllArgsConstructor
public class BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("创建人")
    private Long createUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("更新人")
    private Long updateUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("是否已删除，1是删除，0是正常")
    @TableLogic
    private Integer isDeleted;

    public BaseEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public Long getCreateUser() {
        return this.createUser;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateUser() {
        return this.updateUser;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public Integer getIsDeleted() {
        return this.isDeleted;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setCreateUser(final Long createUser) {
        this.createUser = createUser;
    }

    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateUser(final Long updateUser) {
        this.updateUser = updateUser;
    }

    public void setUpdateTime(final Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setIsDeleted(final Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseEntity)) {
            return false;
        } else {
            BaseEntity other = (BaseEntity) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label107:
                {
                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id == null) {
                            break label107;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label107;
                    }

                    return false;
                }

                Object this$createUser = this.getCreateUser();
                Object other$createUser = other.getCreateUser();
                if (this$createUser == null) {
                    if (other$createUser != null) {
                        return false;
                    }
                } else if (!this$createUser.equals(other$createUser)) {
                    return false;
                }

                label86:
                {
                    Object this$createTime = this.getCreateTime();
                    Object other$createTime = other.getCreateTime();
                    if (this$createTime == null) {
                        if (other$createTime == null) {
                            break label86;
                        }
                    } else if (this$createTime.equals(other$createTime)) {
                        break label86;
                    }

                    return false;
                }

                label79:
                {
                    Object this$updateUser = this.getUpdateUser();
                    Object other$updateUser = other.getUpdateUser();
                    if (this$updateUser == null) {
                        if (other$updateUser == null) {
                            break label79;
                        }
                    } else if (this$updateUser.equals(other$updateUser)) {
                        break label79;
                    }

                    return false;
                }

                label72:
                {
                    Object this$updateTime = this.getUpdateTime();
                    Object other$updateTime = other.getUpdateTime();
                    if (this$updateTime == null) {
                        if (other$updateTime == null) {
                            break label72;
                        }
                    } else if (this$updateTime.equals(other$updateTime)) {
                        break label72;
                    }

                    return false;
                }

                Object this$isDeleted = this.getIsDeleted();
                Object other$isDeleted = other.getIsDeleted();
                if (this$isDeleted == null) {
                    if (other$isDeleted != null) {
                        return false;
                    }
                } else if (!this$isDeleted.equals(other$isDeleted)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BaseEntity;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $createUser = this.getCreateUser();
        result = result * 59 + ($createUser == null ? 43 : $createUser.hashCode());
        Object $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        Object $updateUser = this.getUpdateUser();
        result = result * 59 + ($updateUser == null ? 43 : $updateUser.hashCode());
        Object $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        Object $isDeleted = this.getIsDeleted();
        result = result * 59 + ($isDeleted == null ? 43 : $isDeleted.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "BaseEntity(id=" + this.getId() + ", createUser=" + this.getCreateUser() + ", createTime=" + this.getCreateTime() + ", updateUser=" + this.getUpdateUser() + ", updateTime=" + this.getUpdateTime() + ", isDeleted=" + this.getIsDeleted() + ")";
    }
}

