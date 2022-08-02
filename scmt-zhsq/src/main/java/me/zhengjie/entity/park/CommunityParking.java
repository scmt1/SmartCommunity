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
 * @version $Id: CommunityParking.java, v 1.0 2020年11月23日 下午9:12:05 yongyan.pu Exp $
 */

@Data
public class CommunityParking implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("社区id")
    private String            communityId;

    @ApiModelProperty("社区名称")
    private String            communityName;

    @ApiModelProperty("总共车位")
    private Integer           numberOfParkingSpace;

    @ApiModelProperty("空闲总车位")
    private Integer           numberOfFreeParkingSpace;
}
