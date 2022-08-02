/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.entity.park;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ParkingList.java, v 1.0 2020年11月4日 下午10:08:00 yongyan.pu Exp $
 */

@Data
public class ParkingList implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    private Long              id;

    @ApiModelProperty(value = "停车场名称")
    private String            parkingName;

    @ApiModelProperty(value = "地址")
    private String            address;

    @ApiModelProperty(value = "网格id")
    private String            gridId;

    @ApiModelProperty(value = "网格名称")
    private String            gridName;

    private String            streetId;

    private String            streetName;

    private String            communityId;

    private String            communityName;

    @ApiModelProperty("经纬度范围")
    private List<Double>      region;

    @ApiModelProperty(value = "备注")
    private String            remark;

    private LocalDateTime     createTime;

    private LocalDateTime     updateTime;

}
