/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.entity.park;

import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author yongyan.pu
 * @version $Id: SearchParking.java, v 1.0 2020年10月26日 下午11:36:02 yongyan.pu Exp $
 */

@Data
public class SearchParking implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("停车场id")
    private Long              parkingId;

    @ApiModelProperty(value = "停车场名称")
    private String            parkingName;

    @ApiModelProperty(value = "地址")
    private String            address;

    @ApiModelProperty(value = "经度")
    private Double            longitude;

    @ApiModelProperty(value = "纬度")
    private Double            latitude;

    @JsonIgnore
    private String            longitudeAndLatitude;

    @ApiModelProperty("停车场区域：经纬度")
    private List<Double>      region;

    private Double            distance;

    @ApiModelProperty("总共车位")
    private Integer           numberOfParkingSpace;

    @ApiModelProperty("空闲总车位")
    private Integer           numberOfFreeParkingSpace;

}
