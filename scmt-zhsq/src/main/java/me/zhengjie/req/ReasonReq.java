/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.req;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ReasonReq.java, v 1.0 2020年8月23日 下午5:04:47 yongyan.pu Exp $
 */

@Data
public class ReasonReq implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    private String            rejectReason;

}
