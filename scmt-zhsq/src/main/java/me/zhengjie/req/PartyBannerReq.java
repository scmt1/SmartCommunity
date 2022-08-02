/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.req;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: PartyBannerReq.java, v 1.0 2020年8月13日 下午1:33:14 yongyan.pu Exp $
 */
@Data
public class PartyBannerReq implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "标题不能为空")
    @ApiModelProperty(value = "标题")
    private String            title;
    @NotEmpty(message = "图片地址不能为空")
    @ApiModelProperty(value = "图片地址")
    private String            imgUrl;
}
