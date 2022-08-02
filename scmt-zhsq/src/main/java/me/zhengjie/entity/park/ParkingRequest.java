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
 * 
 * 
 * 
 * @author yongyan.pu
 * @version $Id: ParkingRequest.java, v 1.0 2020年10月26日 下午11:23:59 yongyan.pu Exp $
 */
@Data
public class ParkingRequest implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

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

    //    @ApiModelProperty(value = "经度")
    //    private Double            longitude;
    //
    //    @ApiModelProperty(value = "纬度")
    //    private Double            latitude;

    private List<Double>      region;

    @ApiModelProperty("备注、描述")
    private String            remark;

}
