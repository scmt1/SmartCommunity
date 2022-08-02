/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.req;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: JurisdictionReq.java, v 1.0 2020年8月12日 下午3:34:01 yongyan.pu Exp $
 */

@Data
public class JurisdictionReq implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "名称不能为空")
    private String            name;

}
