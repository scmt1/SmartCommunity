package me.zhengjie.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("部门")
@TableName("grid_dept_type")
public class DeptType extends BaseEntity {

    @ApiModelProperty("名称")
    private String name;

}
