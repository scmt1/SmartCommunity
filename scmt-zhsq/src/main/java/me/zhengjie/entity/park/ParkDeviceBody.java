/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.entity.park;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ParkDeviceBody.java, v 1.0 2020年10月26日 下午3:52:43 yongyan.pu Exp $
 */

@Data
public class ParkDeviceBody implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "停车场编号")
    private String            parkID;

    @ApiModelProperty(value = "车位状态变化时间")
    private String            time;

    @ApiModelProperty(value = "设备 ID")
    private String            deviceID;

    @ApiModelProperty(value = "信号强度")
    private String            rssi;

    @ApiModelProperty(value = "车位状态产生时长")
    private String            passTime;

    @ApiModelProperty(value = "车位状态序号（每产生一个状态累加 1）")
    private String            sequence;

    @ApiModelProperty(value = "电量")
    private String            battary;

    @ApiModelProperty(value = "占用状态 1、占用 2空闲")
    private String            parkingStatu;

    @ApiModelProperty(value = "版本")
    private String            version;

    @ApiModelProperty(value = "0: 正常 1:传感器异常 2：电池电量低于 30% 3：信号覆盖不良")
    private String            errcode;

}
