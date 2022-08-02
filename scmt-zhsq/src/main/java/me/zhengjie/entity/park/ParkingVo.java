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
 * @version $Id: ParkingVo.java, v 1.0 2020年10月27日 下午1:12:39 yongyan.pu Exp $
 */
@Data
public class ParkingVo implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("停车场id")
    private Long              parkingId;

    @ApiModelProperty("停车场名称")
    private String            parkingName;
}
