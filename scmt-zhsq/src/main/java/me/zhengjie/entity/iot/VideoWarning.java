package me.zhengjie.entity.iot;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 视频预警信息
 */
@Data
public class VideoWarning {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("网格id")
    private String gridId;

    @ApiModelProperty("楼盘id")
    private String propertyId;

    @ApiModelProperty("报警等级")
    private Integer alarmGrade;

    @ApiModelProperty("报警类型")
    private Integer alarmType;

    @ApiModelProperty("通道名称")
    private String channelName;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("记录id")
    private Integer alarmId;

    @TableField(exist = false)
    @ApiModelProperty("报警类型名")
    private String alarmTypeName;

    @TableField(exist = false)
    @ApiModelProperty("网格名")
    private String gridName;

    @TableField(exist = false)
    @ApiModelProperty("设备名")
    private String cameraInfoName;


}
