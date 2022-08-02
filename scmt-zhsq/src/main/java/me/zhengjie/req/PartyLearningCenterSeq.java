/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package me.zhengjie.req;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author zoutao
 * @version $Id: PartyLearningCenterSeq.java, v 0.1 2020年8月13日 下午4:54:46 zoutao Exp $
 */
@Data
public class PartyLearningCenterSeq implements Serializable {

    private static final long serialVersionUID = 8048768496347689237L;

    @NotEmpty(message = "title is empty")
    @ApiModelProperty(value = "标题")
    private String            title;

    @NotEmpty(message = "content is empty")
    @ApiModelProperty(value = "内容")
    private String            content;
    @NotEmpty(message = "remark is empty")
    @ApiModelProperty(value = "备注")
    private String            remark;

}
