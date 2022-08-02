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

import java.io.Serializable;
import java.util.Date;

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
@ApiModel(value="TEarlywarning对象")
public class TEarlywarning implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "预警内容")
    private String earlywarningContent;

    @ApiModelProperty(value = "预警方式 0自动 1手动")
    private Integer earlywarningType;

    @ApiModelProperty(value = "重点人员类型")
    private Integer keypersonnelType;

    @ApiModelProperty(value = "重点人员名称")
    private String keypersonnelName;

    @ApiModelProperty(value = "所属社区")
    private String community;

    @ApiModelProperty(value = "预警来源")
    private String earlywarningSource;

    @ApiModelProperty(value = "处理状态 0未处理 1已处理")
    private Integer processingStatus;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    private String updateId;

    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

    @ApiModelProperty(value = "删除人")
    private String deleteId;

    @ApiModelProperty(value = "是否删除 0没有删除 1已删除")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "是否显示 0隐藏 1显示")
    private String isShow;

    @TableField(exist = false)
    private String communityName;

    @TableField(exist = false)
    private String earlywarningSourceName;


}
