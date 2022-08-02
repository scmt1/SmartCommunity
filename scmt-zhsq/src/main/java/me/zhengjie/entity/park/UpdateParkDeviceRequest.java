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
 * @version $Id: UpdateParkDeviceRequest.java, v 1.0 2020年10月26日 下午10:42:59 yongyan.pu Exp $
 */

@Data
public class UpdateParkDeviceRequest implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "停车场编号")
    private String            parkingId;

    @ApiModelProperty("设备id")
    private String            deviceId;

    @ApiModelProperty("设备名称")
    private String            deviceName;

    @ApiModelProperty("是否可用")
    private Boolean           isEnabled;

    @ApiModelProperty(value = "经度")
    private Double            longitude;

    @ApiModelProperty(value = "纬度")
    private Double            latitude;

    @ApiModelProperty("内容")
    private String            remark;

}
