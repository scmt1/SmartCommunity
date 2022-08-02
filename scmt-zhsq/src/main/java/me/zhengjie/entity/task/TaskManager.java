package me.zhengjie.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
  * @Description:    任务管理
  * @Author:         ljj
  * @CreateDate:     2019/5/6 13:38
  */
@Data
@ApiModel
public class TaskManager  {

    @ApiModelProperty("数据id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("网格id")
    private String gridId;

    @ApiModelProperty("单号")
    private String code;

    @ApiModelProperty("类别 （TaskCategory）")
    private Integer categoryId;

    @ApiModelProperty("提交人")
    private Integer submitUserId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("开始时间")
    private Date beginDate;

    @ApiModelProperty("内容")
    private String remark;

    @ApiModelProperty("照片 用,分割")
    private String photos;

    @ApiModelProperty("周期 1：固定；2：零时")
    private Integer cycleFixed;

    @ApiModelProperty("重复周期数")
    private Integer cycleNum;

    @ApiModelProperty("重复周期类型 1：日;2：周;3：月;4：年")
    private Integer cycleType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("下个执行时间")
    private Date nextExecDate;

    @ApiModelProperty("紧急程度")
    private Integer urgentType;

    @ApiModelProperty("状态,0是启用，1是停用,2是删除")
    private Integer status;

    @ApiModelProperty("接单状态 1：可接单；2：不可接单")
    private Integer orderStatus;

    @TableField(exist = false)
    private MultipartFile[] files;

    @TableField(exist = false)
    @ApiModelProperty("执行人ids")
    private String executorIds;

    @TableField(exist = false)
    @ApiModelProperty("建议执行时间")
    private String repairTime;

    @TableField(exist = false)
    @ApiModelProperty("类别名")
    private String categoryName;

    @TableField(exist = false)
    @ApiModelProperty("类别名")
    private String gridName;

    @TableField(exist = false)
    @ApiModelProperty("街道名")
    private String streetName;

    @TableField(exist = false)
    @ApiModelProperty("社区名")
    private String communityName;

}
