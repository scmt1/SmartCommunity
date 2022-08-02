package me.zhengjie.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 表单自动 生成
 * </p>
 *
 * @author zenghu
 * @since 2020-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TFormGenerator对象", description="表单自动 生成")
public class TFormGenerator implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "表单名称")
    private String formName;

    @ApiModelProperty(value = "路径")
    private String jsonStr;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "修改人")
    private String updateId;

    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private int isDelete;

    @ApiModelProperty(value = "删除人")
    private String deleteId;

    @ApiModelProperty(value = "删除时间")
    private Timestamp deleteTime;

    @ApiModelProperty(value = "备注")
    private String remarke;

    @ApiModelProperty(value = "状态")
    private int status;

    @ApiModelProperty(value = "用户名")
    @TableField(exist = false)
    private String username;

    @ApiModelProperty(value = "关联流程ID")
    private String procDefId;

    @ApiModelProperty(value = "关联流程名")
    private String procDefName;

}
