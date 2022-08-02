/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 
 * @author zoutao
 * @version $Id: LessonsMember.java, v 0.1 2020年8月13日 下午4:26:44 zoutao Exp $
 */
@Data
@ApiModel(value = "LessonsMember对象", description = "三学一课听课对象")
@Builder
public class LessonsMemberDto {

    @ApiModelProperty(value = "党员id")
    private Long   id;

    @ApiModelProperty(value = "党员名称")
    private String name;

    @ApiModelProperty(value = "党员头像")
    private String headSculpture;

}
