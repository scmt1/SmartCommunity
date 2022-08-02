package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author dengjie
 * @since 2021-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "TZhsqAnounceInfo对象", description = "公告列表")
public class TZhsqAnounceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "公告id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "公告类型")
    private String infoType;

    @ApiModelProperty(value = "公告排序")
    private String infoSort;

    @ApiModelProperty(value = "公告标题")
    private String infoTitle;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "修改人")
    private String updateId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "删除人")
    private String deleteId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "删除时间")
    private Timestamp deleteTime;

    @ApiModelProperty(value = "内容")
    private String infoContent;

    @ApiModelProperty(value = "发布单位")
    private String infoAuthor;

    @TableLogic
    @ApiModelProperty(value = "是否删除")
    private String isDelete;

    @ApiModelProperty(value = "是否显示 0否 1是")
    private String infoShow;

    @ApiModelProperty(value = "公告类型")
    @TableField(exist = false)
    private TZhsqAnnounceType tZhsqAnnounceType;
}
