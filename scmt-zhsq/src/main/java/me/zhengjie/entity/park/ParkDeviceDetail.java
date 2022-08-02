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
 * @version $Id: ParkDeviceDetail.java, v 1.0 2020年10月27日 下午11:37:12 yongyan.pu Exp $
 */

@Data
public class ParkDeviceDetail implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备id")
    private Long              deviceId;

    @ApiModelProperty(value = "设备名称")
    private String            deviceName;

    @ApiModelProperty(value = "占用状态 1、占用 2空闲")
    private String            parkingStatus;
    @ApiModelProperty(value = "停车场名称")
    private String            parkingName;
    @ApiModelProperty(value = "内容")
    private String            remark;
    @ApiModelProperty(value = "是否可用")
    private String            isEnabled;

}
