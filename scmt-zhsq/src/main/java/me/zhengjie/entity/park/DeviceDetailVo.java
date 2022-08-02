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
 * @version $Id: DeviceDetailVo.java, v 1.0 2020年10月27日 下午5:55:40 yongyan.pu Exp $
 */

@Data
public class DeviceDetailVo implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备id")
    private String            deviceId;

    @ApiModelProperty(value = "设备名称")
    private String            deviceName;

    @ApiModelProperty(value = "占用状态 1、占用 2空闲")
    private String            parkingStatus;

    @ApiModelProperty(value = "经度")
    private Double            longitude;

    @ApiModelProperty(value = "纬度")
    private Double            latitude;

    @ApiModelProperty(value = "是否启用 1、启用 0、停用")
    private Boolean           isEnabled;

    @ApiModelProperty(value = "备注")
    private String            remark;

}
