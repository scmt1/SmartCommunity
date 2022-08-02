/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package me.zhengjie.req;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author zoutao
 * @version $Id: PartyLearningMaterialsSeq.java, v 0.1 2020年8月13日 下午5:21:43 zoutao Exp $
 */
@Data
public class PartyLearningMaterialsSeq implements Serializable {

    /**  */
    private static final long serialVersionUID = -5348202880785898058L;
    @NotEmpty(message = "siteName is empty")
    @ApiModelProperty(value = "网站名称")
    private String            siteName;
    @NotEmpty(message = "siteUrl is empty")
    @ApiModelProperty(value = "网站地址")
    private String            siteUrl;
    @NotNull(message = "sourceType is empty")
    @ApiModelProperty(value = "资源来源(1:两学一做，2：模范宣塑)")
    private Integer           sourceType;
}
