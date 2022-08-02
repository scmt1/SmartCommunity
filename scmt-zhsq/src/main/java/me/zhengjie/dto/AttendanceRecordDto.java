package me.zhengjie.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
* 考勤记录Dto
* */
@Data
public class AttendanceRecordDto {

    @ApiModelProperty("数据id")
    private Long id;

    @ApiModelProperty("状态，1是上班，0是休假")
    private Integer status;

    @ApiModelProperty("时间")
    private String createTime;

    @ApiModelProperty("网格名称")
    private String gridName;

    @ApiModelProperty("街道名")
    private String streetName;

    @ApiModelProperty("网格id")
    private String gridId;

    @ApiModelProperty("档案id")
    private String personId;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("街道 ")
    private String street;

    @ApiModelProperty("社区名")
    private String communityName;

    @ApiModelProperty("部门名")
    private String departmentName;

    @ApiModelProperty("岗位名")
    private String jobName;


}
