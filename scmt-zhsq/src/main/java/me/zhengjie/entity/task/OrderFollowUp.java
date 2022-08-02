package me.zhengjie.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
  * @Description:    工单跟进表
  * @Author:         ly
  * @CreateDate:     2019/5/6 13:50
  */
@Data
@ApiModel
public class OrderFollowUp  {

    @ApiModelProperty("数据id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("报修模块是:BXMK，设备维修:SBWX,任务类别:RWLB")
    private String cood;

    @ApiModelProperty("工单的明细id")
    private Integer detailsId;

    @ApiModelProperty("网格id")
    private String gridId;

    @ApiModelProperty("发布人")
    private Integer publisher;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("跟进内容")
    private String content;

    @ApiModelProperty("照片")
    private String photos;

    @TableField(exist = false)
    @ApiModelProperty("时间")
    private String time;


}
