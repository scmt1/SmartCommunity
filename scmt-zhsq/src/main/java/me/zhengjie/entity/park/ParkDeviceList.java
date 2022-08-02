/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.entity.park;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ParkDeviceList.java, v 1.0 2020年10月26日 下午11:01:20 yongyan.pu Exp $
 */
@Data
public class ParkDeviceList implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    private Long              id;

    @ApiModelProperty(value = "设备id")
    private String            deviceId;

    @ApiModelProperty(value = "设备名称")
    private String            deviceName;

    @ApiModelProperty(value = "是否可用 true 可用 false 不可用")
    private Boolean           isEnabled;

    @ApiModelProperty(value = "停车场id")
    private Long              parkingId;

    @ApiModelProperty(value = "停车场名称")
    private String            parkingName;

    @ApiModelProperty(value = "经度")
    private Double            longitude;

    @ApiModelProperty(value = "纬度")
    private Double            latitude;

    @ApiModelProperty("占用状态 1、占用 2空闲")
    private Integer           parkingStatus;

    private Date              createTime;

    private String            remark;

}
