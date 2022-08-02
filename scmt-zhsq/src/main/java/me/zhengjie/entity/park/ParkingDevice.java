package me.zhengjie.entity.park;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author dengjie
 * @since 2020-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ParkingDevice对象")
public class ParkingDevice implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long              id;

    @ApiModelProperty(value = "停车场编号")
    private String            parkingId;

    @ApiModelProperty(value = "车位状态变化时间")
    private String            time;

    @ApiModelProperty(value = "设备 ID")
    private String            deviceId;

    @ApiModelProperty(value = "设备名称")
    private String            deviceName;

    @ApiModelProperty(value = "信号强度")
    private String            rssi;

    @ApiModelProperty(value = "车位状态产生时长")
    private String            passTime;

    @ApiModelProperty(value = "车位状态序号（每产生一个状态累加 1）")
    private String            sequence;

    @ApiModelProperty(value = "电量")
    private String            battary;

    @ApiModelProperty(value = "占用状态 1、占用 2空闲")
    private String            parkingStatus;

    @ApiModelProperty(value = "版本")
    private String            version;

    @ApiModelProperty(value = "0: 正常 1:传感器异常 2：电池电量低于 30% 3：信号覆盖不良")
    private String            errcode;

    @ApiModelProperty(value = "经度")
    private Double            longitude;

    @ApiModelProperty(value = "纬度")
    private Double            latitude;

    @ApiModelProperty(value = "是否启用 1、启用 0、停用")
    private Boolean           isEnabled;

    @ApiModelProperty(value = "备注")
    private String            remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime     createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime     updateTime;

    @ApiModelProperty(value = "0、正常 1、删除")
    private Boolean           isDeleted;

}
