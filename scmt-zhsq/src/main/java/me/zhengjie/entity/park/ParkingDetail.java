/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.entity.park;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ParkingDetail.java, v 1.0 2020年10月27日 下午5:51:08 yongyan.pu Exp $
 */

@Data
public class ParkingDetail implements Serializable {

    /**  */
    private static final long    serialVersionUID = 1L;

    @ApiModelProperty(value = "停车场id")
    private Long                 parkingId;

    @ApiModelProperty(value = "停车场名称")
    private String               parkingName;

    @ApiModelProperty(value = "停车场地址")
    private String               address;

    @ApiModelProperty(value = "经度")
    private Double               longitude;

    @ApiModelProperty(value = "纬度")
    private Double               latitude;

    private List<Double>         region;

    @ApiModelProperty("总共车位")
    private Integer              numberOfParkingSpace;

    @ApiModelProperty("空闲总车位")
    private Integer              numberOfFreeParkingSpace;

    @ApiModelProperty("设备列表")
    private List<DeviceDetailVo> deviceList;

}
