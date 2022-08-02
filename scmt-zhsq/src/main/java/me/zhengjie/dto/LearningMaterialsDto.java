package me.zhengjie.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;



@ApiModel(value = "LearningMaterialsDto对象", description = "两学一做,模范宣塑 学习对象")
@Data
@Builder
public class LearningMaterialsDto implements Serializable{

    /**  */
    private static final long serialVersionUID = -4584459111180045667L;
    

    @ApiModelProperty(value = "网站名称")
    private String siteName;

    @ApiModelProperty(value = "网站地址")
    private String siteUrl;
    

}
