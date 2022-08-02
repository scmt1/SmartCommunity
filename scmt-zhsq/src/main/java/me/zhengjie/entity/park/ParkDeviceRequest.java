/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.entity.park;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ParkDeviceRequest.java, v 1.0 2020年10月26日 下午3:42:36 yongyan.pu Exp $
 */
@Data
public class ParkDeviceRequest implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    private String            cmd;

    private ParkDeviceBody    body;

}
