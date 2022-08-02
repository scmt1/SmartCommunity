package me.zhengjie.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "数据字典")
public class GridDictionary {

    @ApiModelProperty(value = "字典字段名")
    private String fileName;

    @ApiModelProperty(value = "字典类型名")
    private String typeName;

    @ApiModelProperty(value = "字典名")
    private String name;

    @ApiModelProperty(value = "字典值")
    private String number;

}
