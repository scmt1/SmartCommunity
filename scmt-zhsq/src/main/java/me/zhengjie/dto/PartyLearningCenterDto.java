/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author zoutao
 * @version $Id: PartyLearningCenterDto.java, v 0.1 2020年8月13日 下午10:35:03 zoutao Exp $
 */
@Data
public class PartyLearningCenterDto {

    @ApiModelProperty(value = "id")
    private String  id;

    @ApiModelProperty(value = "标题")
    private String  title;
    //
    //    @ApiModelProperty(value = "内容")
    //    private String  content;
    //
    //    @ApiModelProperty(value = "备注")
    //    private String  remark;
    private Date    createTime;

    @ApiModelProperty(value = "是否已完成学习")
    private Boolean isLearn;
}
